package org.hango.cloud.envoy.infra.virtualgateway.controller;

import org.hango.cloud.common.infra.base.controller.AbstractController;
import org.hango.cloud.common.infra.base.errorcode.CommonErrorCode;
import org.hango.cloud.common.infra.base.errorcode.ErrorCode;
import org.hango.cloud.common.infra.base.meta.BaseConst;
import org.hango.cloud.common.infra.base.meta.Result;

import org.hango.cloud.common.infra.virtualgateway.service.IVirtualGatewayInfoService;
import org.hango.cloud.envoy.infra.virtualgateway.dto.KubernetesGatewayDTO;
import org.hango.cloud.envoy.infra.virtualgateway.dto.KubernetesGatewayHttpRouteDTO;
import org.hango.cloud.envoy.infra.virtualgateway.service.IKubernetesGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = BaseConst.HANGO_VIRTUAL_GATEWAY_V1_PREFIX)
@Validated
public class KubernetesGatewayController extends AbstractController {

    @Autowired
    private IKubernetesGatewayService kubernetesGatewayService;

    @Autowired
    private IVirtualGatewayInfoService virtualGatewayInfoService;
    /**
     * 刷新虚拟网关gateway api/ingress
     * @return
     */
    @GetMapping(params = {"Action=RefreshKubernetesGateway"})
    public Object refreshKubernetesGateway() {
        ErrorCode errorCode = kubernetesGatewayService.refreshK8sGateway();
        if (!CommonErrorCode.SUCCESS.equals(errorCode)) {
            return apiReturn(Result.err(errorCode));
        }
        return apiReturn(new Result(errorCode));
    }

    /**
     * 查询k8s gateway info
     * @return
     */
    @GetMapping(params = {"Action=DescribeKubernetesGateway"})
    public Object describeKubernetesGateway(@RequestParam("VirtualGatewayId") long virtualGwId) {
        List<KubernetesGatewayDTO> kubernetesGatewayList = kubernetesGatewayService.getKubernetesGatewayList(virtualGwId);
        return apiReturn(new Result(kubernetesGatewayList));
    }

    /**
     * 查询k8s gateway yaml
     * @return
     */
    @GetMapping(params = {"Action=DescribeKubernetesGatewayYaml"})
    public Object describeKubernetesGatewayYaml(@RequestParam("VirtualGatewayId") long virtualGwId) {
        String kubernetesGatewayYaml = kubernetesGatewayService.getKubernetesGatewayYaml(virtualGwId);
        return apiReturn(new Result(kubernetesGatewayYaml));
    }

    /**
     * 查询K8s httpRoute列表
     *
     * @param virtualGatewayId 虚拟网关的ID
     * @return 封装好的httpRouteDTO列表对象
     */
    @GetMapping(params = {"Action=DescribeHTTPRoute"})
    public Object describeHTTPRoute(@RequestParam(name = "VirtualGatewayId") long virtualGatewayId) {
        List<KubernetesGatewayHttpRouteDTO> kubernetesGatewayHTTPRouteList = kubernetesGatewayService.getKubernetesGatewayHTTPRouteList(virtualGatewayId);
        return apiReturn(new Result<>(kubernetesGatewayHTTPRouteList));
    }
}
