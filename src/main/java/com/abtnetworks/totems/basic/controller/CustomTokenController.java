////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package com.abtnetworks.totems.basic.controller;
//
//import com.abtnetworks.data.totems.log.client.LogClientSimple;
//import com.abtnetworks.data.totems.log.common.enums.BusinessLogType;
//import com.abtnetworks.data.totems.log.common.enums.LogLevel;
//import com.abtnetworks.totems.basic.entity.mysql.UmsPwdEntity;
//import com.abtnetworks.totems.basic.entity.mysql.UmsUserEntity;
//import com.abtnetworks.totems.basic.entity.mysql.UmsUserSsoTokenEntity;
//import com.abtnetworks.totems.basic.request.UmsUserSsoTokenQueryRequest;
//import com.abtnetworks.totems.basic.service.UserInfoService;
//import com.abtnetworks.totems.common.BaseController;
//import com.abtnetworks.totems.common.BaseRequest;
//import com.abtnetworks.totems.common.ReturnT;
//import com.abtnetworks.totems.common.utils.DateUtils;
//import com.abtnetworks.totems.common.utils.EncryptUtils;
//import com.abtnetworks.totems.common.utils.IdGen;
//import com.abtnetworks.totems.common.utils.StringUtils;
//import com.abtnetworks.totems.sso.config.ClientDetailsStoreMap;
//import com.github.pagehelper.PageInfo;
//import io.swagger.annotations.Api;
//import java.nio.charset.StandardCharsets;
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import org.apache.commons.codec.binary.Base64;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Api("???????????????token")
//@RestController
//@RequestMapping({"/customToken"})
//public class CustomTokenController extends BaseController {
//    @Autowired
//    private ClientDetailsStoreMap clientDetailsStoreMap;
//    @Autowired
//    private TokenEndpoint tokenEndpointService;
//    @Autowired
//    private TokenStore tokenStore;
//    @Value("${oauth2.client.clientId}")
//    private String clientId;
//    @Value("${thirdPart.oauth2.client.clientId}")
//    private String thirdPartClientId;
//    @Autowired
//    private LogClientSimple logClientSimple;
//    @Autowired
//    private UserInfoService userInfoService;
//    private static final Integer DEFAULT_TOKEN_VALIDITY_SECONDS = 43200;
//    private static final String OAUTH2_TYPE_PASSWORD = "password";
//    private static final String OAUTH2_TYPE_CLIENT_CREDENTIALS = "client_credentials";
//
//    public CustomTokenController() {
//    }
//
//    public String creatThirdPartToken(Authentication auth, Integer validitySeconds, String type, String username, String password) throws HttpRequestMethodNotSupportedException {
//        String tokenValue = null;
//
//        try {
//            ClientDetails clientDetails = null;
//            Map<String, String> parameters = new HashMap();
//            if ("password".equals(type)) {
//                clientDetails = (ClientDetails)this.clientDetailsStoreMap.clientDetailsStore().get(this.clientId);
//                parameters.put("client_id", clientDetails.getClientId());
//                parameters.put("client_secret", clientDetails.getClientSecret());
//                parameters.put("grant_type", "password");
//                parameters.put("username", username);
//                String base64RawPassword1 = new String(Base64.encodeBase64(password.getBytes(StandardCharsets.UTF_8)), "UTF-8");
//                String base64RawPassword2 = new String(Base64.encodeBase64(base64RawPassword1.getBytes(StandardCharsets.UTF_8)), "UTF-8");
//                parameters.put("password", base64RawPassword2);
//            } else if ("client_credentials".equals(type)) {
//                clientDetails = (ClientDetails)this.clientDetailsStoreMap.clientDetailsStore().get(this.thirdPartClientId);
//                parameters.put("client_id", clientDetails.getClientId());
//                parameters.put("client_secret", clientDetails.getClientSecret());
//                parameters.put("grant_type", "client_credentials");
//            }
//
//            Authentication authentication = new UsernamePasswordAuthenticationToken(clientDetails.getClientId(), clientDetails.getClientSecret(), clientDetails.getAuthorities());
//            ResponseEntity<OAuth2AccessToken> entity = this.tokenEndpointService.postAccessToken(authentication, parameters);
//            String accessTokenValue = ((OAuth2AccessToken)entity.getBody()).getValue();
//            DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken)entity.getBody();
//            if (accessToken == null) {
//                throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
//            }
//
//            if (accessToken.isExpired()) {
//                this.tokenStore.removeAccessToken(accessToken);
//                throw new InvalidTokenException("Access token expired: " + accessTokenValue);
//            }
//
//            OAuth2Authentication result = this.tokenStore.readAuthentication(accessToken);
//            if (result == null) {
//                throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
//            }
//
//            accessToken.setExpiration(new Date(System.currentTimeMillis() + (long)validitySeconds * 1000L));
//            this.tokenStore.storeAccessToken(accessToken, result);
//            tokenValue = accessToken.getValue();
//            String userName = "????????????";
//            if (auth != null && auth.getName() != null) {
//                userName = auth.getName();
//            }
//
//            String logInfo = String.format("[%s] ????????????/?????? ?????????[%s] ???token?????????[%s] ????????????:[%s] ???????????????[%s]", userName, type, tokenValue, validitySeconds, DateUtils.getDateTime());
//            this.logger.warn(logInfo);
//            this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYSTEM_MANAGE.getId(), logInfo);
//        } catch (Exception var16) {
//            this.logger.error("", var16);
//        }
//
//        return tokenValue;
//    }
//
//    public boolean clearThirdPartToken(Authentication auth, String type, String userName) {
//        try {
//            ClientDetails clientDetails = null;
//            Collection<OAuth2AccessToken> collection = null;
//            if ("password".equals(type)) {
//                clientDetails = (ClientDetails)this.clientDetailsStoreMap.clientDetailsStore().get(this.clientId);
//                collection = this.tokenStore.findTokensByClientIdAndUserName(clientDetails.getClientId(), userName);
//            } else if ("client_credentials".equals(type)) {
//                clientDetails = (ClientDetails)this.clientDetailsStoreMap.clientDetailsStore().get(this.thirdPartClientId);
//                collection = this.tokenStore.findTokensByClientId(clientDetails.getClientId());
//            }
//
//            Iterator var6 = collection.iterator();
//
//            while(var6.hasNext()) {
//                OAuth2AccessToken tmpAccessToken = (OAuth2AccessToken)var6.next();
//                OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(tmpAccessToken.getValue());
//                if (oAuth2AccessToken != null) {
//                    if (oAuth2AccessToken.getRefreshToken() != null) {
//                        this.tokenStore.removeRefreshToken(oAuth2AccessToken.getRefreshToken());
//                        this.tokenStore.removeAccessTokenUsingRefreshToken(oAuth2AccessToken.getRefreshToken());
//                    }
//
//                    this.tokenStore.removeAccessToken(oAuth2AccessToken);
//                }
//            }
//
//            String operationUser = "????????????";
//            if (auth != null && auth.getName() != null) {
//                operationUser = auth.getName();
//            }
//
//            String logInfo = String.format("??????[%s] ??????[%s]?????????token ???????????????[%s]????????????[%s]", operationUser, type, DateUtils.getDateTime(), userName == null ? "???" : userName);
//            this.logger.warn(logInfo);
//            this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYSTEM_MANAGE.getId(), logInfo);
//            return true;
//        } catch (Exception var9) {
//            this.logger.error("clearToken:", var9);
//            return false;
//        }
//    }
//
//    @PostMapping({"/insert"})
//    public ReturnT<String> insert(Authentication auth, @RequestBody UmsUserSsoTokenEntity entity) {
//        try {
//            if (entity != null && !StringUtils.isAnyBlank(new CharSequence[]{entity.getType(), entity.getTokenValiditySeconds()})) {
//                if (Integer.valueOf(entity.getTokenValiditySeconds()) < DEFAULT_TOKEN_VALIDITY_SECONDS) {
//                    return new ReturnT(500, "??????????????????????????????12?????? 43200??????");
//                } else {
//                    if ("password".equals(entity.getType())) {
//                        if (StringUtils.isAnyBlank(new CharSequence[]{entity.getUserName(), entity.getUserPassword(), entity.getTokenValiditySeconds()})) {
//                            return new ReturnT(500, "?????????????????????????????????????????????");
//                        }
//
//                        UmsUserEntity userEntity = this.userInfoService.getUmsUserEntityById(entity.getUserName());
//                        if (userEntity != null && StringUtils.isNotBlank(userEntity.getId())) {
//                            return new ReturnT(500, "??????????????????");
//                        }
//
//                        UmsUserEntity tmpUser = this.userInfoService.getUmsUserEntityById(StringUtils.isBlank(auth.getName()) ? "superadmin" : auth.getName());
//                        UmsUserEntity umsUserEntity = new UmsUserEntity();
//                        umsUserEntity.setId(entity.getUserName());
//                        umsUserEntity.setName(entity.getUserName());
//                        umsUserEntity.setEnabled("0");
//                        umsUserEntity.setBranchLevel(tmpUser.getBranchLevel());
//                        umsUserEntity.setRoleUuid("004");
//                        umsUserEntity.setCreatedUser(tmpUser.getId());
//                        ReturnT rT1 = this.userInfoService.insertUmsUserEntity(umsUserEntity);
//                        if (500 == rT1.getCode()) {
//                            return new ReturnT(500, "????????????????????????????????????????????????");
//                        }
//
//                        UmsPwdEntity pwdEntity = new UmsPwdEntity();
//                        pwdEntity.setId(entity.getUserName());
//                        pwdEntity.setPassword(EncryptUtils.Encrypt(entity.getUserPassword(), "SHA-256"));
//                        ReturnT rT2 = this.userInfoService.insertUmsPwdEntity(pwdEntity);
//                        if (500 == rT2.getCode()) {
//                            return new ReturnT(500, "????????????????????????????????????????????????");
//                        }
//                    } else {
//                        if (!"client_credentials".equals(entity.getType())) {
//                            return new ReturnT(500, "type??????????????????");
//                        }
//
//                        UmsUserSsoTokenEntity userSsoTokenEntity = this.userInfoService.getUserSsoTokenEntityByUserName("DEFAULT_THIRD_PARTY_ORGANIZATION");
//                        if (userSsoTokenEntity != null) {
//                            return new ReturnT(500, "???????????????????????????token????????????????????????????????????");
//                        }
//
//                        entity.setUserName("DEFAULT_THIRD_PARTY_ORGANIZATION");
//                        if (StringUtils.isAnyBlank(new CharSequence[]{entity.getTokenValiditySeconds()})) {
//                            return new ReturnT(500, "?????????????????????????????????");
//                        }
//                    }
//
//                    entity.setUuid(IdGen.uuid());
//                    entity.setUmsUserId(entity.getUserName());
//                    String tokenValue = this.creatThirdPartToken(auth, Integer.valueOf(entity.getTokenValiditySeconds()), entity.getType(), entity.getUserName(), entity.getUserPassword());
//                    if (StringUtils.isBlank(tokenValue)) {
//                        return new ReturnT(500, "token????????????????????????????????????");
//                    } else {
//                        entity.setTokenValue(tokenValue);
//                        return this.userInfoService.insertUserSsoTokenEntity(entity);
//                    }
//                }
//            } else {
//                return new ReturnT(500, "???????????????????????????????????????type???????????????");
//            }
//        } catch (Exception var9) {
//            this.logger.error("", var9);
//            return ReturnT.FAIL;
//        }
//    }
//
//    @PostMapping({"/pageList"})
//    public ReturnT pageList(@RequestBody BaseRequest<UmsUserSsoTokenQueryRequest> request) {
//        try {
//            if (request == null) {
//                return new ReturnT(500, "??????????????????");
//            } else {
//                PageInfo<UmsUserSsoTokenEntity> pageInfo = this.userInfoService.pageFindUserSsoTokenEntityList((UmsUserSsoTokenEntity)request.getVal(), request.getPage(), request.getLimit());
//                return new ReturnT(pageInfo);
//            }
//        } catch (Exception var3) {
//            this.logger.error("", var3);
//            return ReturnT.FAIL;
//        }
//    }
//
//    @PostMapping({"/delete"})
//    public ReturnT<String> delete(Authentication auth, int id) {
//        try {
//            UmsUserSsoTokenEntity userSsoTokenEntity = this.userInfoService.getUserSsoTokenEntityById(id);
//            if (userSsoTokenEntity == null) {
//                return new ReturnT(500, "???token??????????????????");
//            } else {
//                ReturnT rT1 = this.userInfoService.deleteUserSsoTokenEntityById(id);
//                if ("password".equals(userSsoTokenEntity.getType())) {
//                    ReturnT rT2 = this.userInfoService.deleteUmsUserEntityById(userSsoTokenEntity.getUmsUserId());
//                    ReturnT rT3 = this.userInfoService.deleteUmsPwdEntityById(userSsoTokenEntity.getUmsUserId());
//                    String msg = String.format("deleteUmsUserEntityById ??????????????? %s , deleteUmsPwdEntityById ??????????????? %s", rT2.toString(), rT3.toString());
//                    this.logger.error(msg);
//                }
//
//                boolean result = this.clearThirdPartToken(auth, userSsoTokenEntity.getType(), userSsoTokenEntity.getUserName());
//                if (200 == rT1.getCode() && result) {
//                    this.logger.info("??????token???" + result);
//                    return ReturnT.SUCCESS;
//                } else {
//                    String msg = String.format("deleteUserSsoTokenEntityById ??????????????? %s ", rT1.toString());
//                    this.logger.error(msg);
//                    return ReturnT.FAIL;
//                }
//            }
//        } catch (Exception var8) {
//            this.logger.error("", var8);
//            return ReturnT.FAIL;
//        }
//    }
//}
