//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.abtnetworks.totems.layer.controller;

import com.abtnetworks.data.totems.log.client.LogClientSimple;
import com.abtnetworks.data.totems.log.common.enums.BusinessLogType;
import com.abtnetworks.data.totems.log.common.enums.LogLevel;
import com.abtnetworks.totems.common.TotemsReturnT;
import com.abtnetworks.totems.common.ro.ResultRO;
import com.abtnetworks.totems.common.web.controller.BaseController;
import com.abtnetworks.totems.layer.dto.LayerSplitSubnetRO;
import com.abtnetworks.totems.layer.dto.ListSubnetsDTO;
import com.abtnetworks.totems.layer.service.WhaleAgencyService;
import com.abtnetworks.totems.layer.vo.MemoryPagination;
import com.abtnetworks.totems.layer.vo.PathAnalyzeModifyVO;
import com.abtnetworks.totems.layer.vo.RealSubnetModifyVO;
import com.abtnetworks.totems.layer.vo.SubnetListVO;
import com.abtnetworks.totems.layer.vo.TrafficFileVO;
import com.abtnetworks.totems.layer.vo.ZoneVO;
import com.abtnetworks.totems.whale.baseapi.dto.DetailPathSubnetDTO;
import com.abtnetworks.totems.whale.baseapi.dto.SubnetDTO;
import com.abtnetworks.totems.whale.baseapi.ro.DeviceSummaryRO;
import com.abtnetworks.totems.whale.baseapi.ro.InterfaceExtendDataRO;
import com.abtnetworks.totems.whale.baseapi.ro.InterfacesRO;
import com.abtnetworks.totems.whale.baseapi.ro.SubnetLinkedDeviceRO;
import com.abtnetworks.totems.whale.baseapi.ro.SubnetListRO;
import com.abtnetworks.totems.whale.policy.dto.AccessQueryDTO;
import com.abtnetworks.totems.whale.policy.dto.PathAnalyzeDTO;
import com.abtnetworks.totems.whale.policy.ro.AccessQueryRO;
import com.abtnetworks.totems.whale.policy.ro.AccessQueryTrafficResultRO;
import com.abtnetworks.totems.whale.policy.ro.AnalysisCancel;
import com.abtnetworks.totems.whale.policy.ro.AnalysisStartRO;
import com.abtnetworks.totems.whale.system.ro.TaskStatusRO;
import com.abtnetworks.totems.whale.topo.dto.LinkVpnDTO;
import com.abtnetworks.totems.whale.topo.dto.SplitSubnetDTO;
import com.abtnetworks.totems.whale.topo.ro.LinkVpnRO;
import com.abtnetworks.totems.whale.topo.ro.SplitSubnetDataRO;
import com.abtnetworks.totems.whale.topo.ro.TopoUnDoRO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/whale"})
@Api("whale???????????????")
@Validated
public class WhaleAgencyController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(WhaleAgencyController.class);
    @Resource
    WhaleAgencyService whaleAgencyService;
    @Resource
    private LogClientSimple logClientSimple;

    public WhaleAgencyController() {
    }

    @PostMapping({"/GET/device/subnets"})
    @ApiOperation("?????????????????????????????????2")
    public TotemsReturnT<MemoryPagination<InterfaceExtendDataRO>> listSubnets(@RequestBody ListSubnetsDTO listSubnetsDTO) throws Exception {
        if (listSubnetsDTO.getPageNum() == null || listSubnetsDTO.getPageSize() == null) {
            listSubnetsDTO.setPageNum(1);
            listSubnetsDTO.setPageSize(20);
        }

        return new TotemsReturnT(this.whaleAgencyService.listSubnets(listSubnetsDTO));
    }

    @GetMapping({"/GET/subnet/linkedDevice"})
    @ApiOperation("???????????????????????????")
    public SubnetLinkedDeviceRO listSubnetLinkedDevice(@ApiParam("?????????uuid") @NotNull String subnetUuid) throws Exception {
        return this.whaleAgencyService.listSubnetLinkedDevice(subnetUuid);
    }

    @GetMapping({"/GET/devices/summary"})
    @ApiOperation("????????????????????????")
    public DeviceSummaryRO getDevicesSummary(@ApiParam("??????????????????") @RequestParam(defaultValue = "false") Boolean skipHost) throws Exception {
        return this.whaleAgencyService.getDevicesSummary(skipHost);
    }

    @GetMapping({"/GET/device/zones"})
    @ApiOperation("????????????????????????")
    public ZoneVO getDeviceZoneObject(@ApiParam("?????????uuid") @NotNull String deviceUuid, @ApiParam("??????uuid") String zoneUuid) throws Exception {
        return this.whaleAgencyService.getDeviceZoneObject(deviceUuid, zoneUuid);
    }

    @PostMapping({"/GET/access/query"})
    @ApiOperation("???????????????")
    public AccessQueryRO getAccessQueryObject(@RequestBody AccessQueryDTO accessQueryDTO) throws Exception {
        return this.whaleAgencyService.getAccessQueryObject(accessQueryDTO);
    }

    @PostMapping({"/GET/access/query-export"})
    @ApiOperation("???????????????")
    public TrafficFileVO getAccessQueryTraffic(@RequestBody AccessQueryDTO accessQueryDTO) {
        return this.whaleAgencyService.queryExport(accessQueryDTO);
    }

    @PostMapping({"/GET/access/query-traffic"})
    @ApiOperation("???????????????")
    public AccessQueryTrafficResultRO queryExport(@RequestBody AccessQueryDTO accessQueryDTO) {
        return this.whaleAgencyService.getAccessQueryTraffic(accessQueryDTO);
    }

    @PostMapping({"/GET/detailedPath/run", "/GET/detailedPath/run/{isFilterNoPath}"})
    @ApiOperation("???????????????")
    public PathAnalyzeModifyVO getDetailPathObject(@RequestBody PathAnalyzeDTO analyzeDTO, @PathVariable(required = false) Boolean isFilterNoPath) throws Exception {
        return this.whaleAgencyService.getDetailPathObject(analyzeDTO, isFilterNoPath);
    }

    @GetMapping({"/GET/node/navigation"})
    @ApiOperation("???????????????????????????????????????")
    public Map<String, Map<String, Map<String, Object>>> listTopoNodeByLayerUuid(@ApiParam("??????uuid") @NotNull String layerUuid, @ApiParam("??????????????????") @NotNull Boolean isFilter, Authentication attributePrincipal, String branchLevel) throws Exception {
        String userId = null;
        if (attributePrincipal != null) {
            userId = attributePrincipal.getName();
        }

        return this.whaleAgencyService.listTopoNodeByLayerUuidMetoo(layerUuid, isFilter, userId, branchLevel);
    }

    @PutMapping({"/PUT/topo/action/splitSubnet"})
    @ApiOperation("????????????")
    public LayerSplitSubnetRO splitSubnet(@RequestBody SplitSubnetDTO splitSubnetDTO) throws Exception {
        LayerSplitSubnetRO splitSubnetRO = this.whaleAgencyService.splitSubnet(splitSubnetDTO);
        if (splitSubnetRO.getSuccess()) {
            this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYNTHESIZE_CONFIGURE.getId(), "??????????????????");
        } else {
            this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYNTHESIZE_CONFIGURE.getId(), "??????????????????");
        }

        return splitSubnetRO;
    }

    @PutMapping({"/PUT/topo/action/linkVpn"})
    @ApiOperation("??????vpn??????")
    public LinkVpnRO linkVpn(@RequestBody LinkVpnDTO linkVpnDTO) throws Exception {
        LinkVpnRO linkVpnRO = this.whaleAgencyService.linkVpn(linkVpnDTO);
        if (linkVpnRO.getSuccess()) {
            this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYNTHESIZE_CONFIGURE.getId(), "??????vpn????????????");
        } else {
            this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYNTHESIZE_CONFIGURE.getId(), "??????vpn????????????");
        }

        return linkVpnRO;
    }

    @GetMapping({"/GET/topo/action/linkVpn"})
    @ApiOperation("??????vpn??????")
    public LinkVpnRO getLinkVpn() throws Exception {
        return this.whaleAgencyService.getLinkVpn();
    }

    @DeleteMapping({"/DELETE/topo/action/linkVpn"})
    @ApiOperation("??????vpn??????")
    public LinkVpnRO deleteLinkVpn(@ApiParam("vpn???uuid") @NotNull String uuid) throws Exception {
        LinkVpnRO linkVpnRO = this.whaleAgencyService.deleteLinkVpn(uuid);
        if (linkVpnRO.getSuccess()) {
            this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYNTHESIZE_CONFIGURE.getId(), "??????vpn????????????");
        } else {
            this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYNTHESIZE_CONFIGURE.getId(), "??????vpn????????????");
        }

        return linkVpnRO;
    }

    @GetMapping({"/GET/vpn/subnet"})
    @ApiOperation("???vpn?????????????????????")
    public Map<String, Object> listVpnSubnets(@ApiParam("?????????uuid") @NotNull String deviceUuid) throws Exception {
        return this.whaleAgencyService.listVpnSubnets(deviceUuid);
    }

    @GetMapping({"/GET/subnets"})
    @ApiOperation("????????????????????????")
    public SubnetListVO getSubnetObject(@ApiParam("?????????uuid") @RequestParam(required = false) String subnetUuid, @ApiParam("?????????ip4Address") @RequestParam(required = false) String ip4Addr) throws Exception {
        return this.whaleAgencyService.getSubnetObject(subnetUuid, ip4Addr);
    }

    /** @deprecated */
    @PutMapping({"/PUT/subnet"})
    @ApiOperation("????????????")
    @Deprecated
    public SubnetListRO updateSubnetObject(@RequestBody SubnetDTO subnetDTO) throws Exception {
        SubnetListRO subnetListRO = this.whaleAgencyService.updateSubnetObject(subnetDTO);
        if (subnetListRO.getSuccess()) {
            this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYNTHESIZE_CONFIGURE.getId(), "??????????????????");
        } else {
            this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYNTHESIZE_CONFIGURE.getId(), "??????????????????");
        }

        return subnetListRO;
    }

    @PutMapping({"/PUT/topo/action/undo/splitSubnet"})
    @ApiOperation("???????????????????????????uuid")
    public TopoUnDoRO splitSubnetUnDo(@ApiParam("?????????uuid") String opUuid) throws Exception {
        return this.whaleAgencyService.splitSubnetUnDo(opUuid);
    }

    @PostMapping({"/POST/topo/action/all-split-subnet-summary"})
    @ApiOperation("??????????????????????????????")
    public ResultRO<List<SplitSubnetDataRO>> getAllSplitSubnetSummary() throws Exception {
        return this.whaleAgencyService.getAllSplitSubnetSummary();
    }

    @PutMapping({"/PUT/analysis/access/start"})
    @ApiOperation("???????????????????????????")
    public AnalysisStartRO analysisAccessStart() throws Exception {
        log.info("???????????????????????????");
        this.logClientSimple.addBusinessLog(LogLevel.INFO.getId(), BusinessLogType.SYNTHESIZE_CONFIGURE.getId(), "???????????????????????????");
        return this.whaleAgencyService.analysisAccessStart();
    }

    @PutMapping({"/PUT/analysis/access/cancel"})
    @ApiOperation("???????????????????????????")
    public AnalysisCancel analysisAccessCancel() throws Exception {
        return this.whaleAgencyService.analysisAccessCancel();
    }

    @GetMapping({"/GET/task/run/{taskType}/{taskRunUuid}", "/GET/task/run/{taskType}"})
    @ApiOperation("????????????????????????")
    @ApiImplicitParams({@ApiImplicitParam(
            name = "taskType",
            value = "??????????????????????????? RULE_CHECK, ACCESS_ANALYSIS, POLICY_ANALYSIS, GENERIC_BPC, GENERIC",
            required = true,
            dataType = "string",
            paramType = "path"
    ), @ApiImplicitParam(
            name = "taskRunUuid",
            value = "??????????????????UUID",
            dataType = "string",
            paramType = "path"
    ), @ApiImplicitParam(
            name = "page",
            value = "??????",
            dataType = "int",
            paramType = "query"
    ), @ApiImplicitParam(
            name = "psize",
            value = "????????????",
            dataType = "int",
            paramType = "query"
    )})
    public TaskStatusRO getTaskStatus(@PathVariable String taskType, @PathVariable(required = false) String taskRunUuid, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "50") Integer psize) throws Exception {
        return this.whaleAgencyService.getTaskStatus(taskType, taskRunUuid, page, psize);
    }

    @PostMapping({"/GET/detailedPath/realSubnet/find"})
    @ApiOperation("??????????????????")
    public RealSubnetModifyVO getDetailPathSubnet(@RequestBody DetailPathSubnetDTO detailPathSubnetDTO) throws Exception {
        return this.whaleAgencyService.getDetailPathSubnet(detailPathSubnetDTO);
    }

    @PostMapping({"/GET/device/interface/{uuid}"})
    @ApiOperation("????????????????????????")
    public ResultRO<List<InterfacesRO>> findNetInterfaceByUuid(@PathVariable String uuid) {
        return this.whaleAgencyService.findNetInterfaceByUuid(uuid);
    }
}
