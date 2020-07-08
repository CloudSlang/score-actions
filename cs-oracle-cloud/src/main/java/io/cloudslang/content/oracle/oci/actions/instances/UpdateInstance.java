package io.cloudslang.content.oracle.oci.actions.instances;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCIUpdateInstanceInputs;
import io.cloudslang.content.oracle.oci.services.InstanceImpl;
import io.cloudslang.content.oracle.oci.utils.Descriptions;
import io.cloudslang.content.oracle.oci.utils.HttpUtils;
import io.cloudslang.content.oracle.oci.utils.InputsValidation;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.CreateInstanceConstants.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.UpdateInstanceConstants.UPDATE_INSTANCE_OPERATION_NAME;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.Common.*;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.CreateInstance.*;
import static io.cloudslang.content.oracle.oci.utils.Descriptions.UpdateInstance.UPDATE_INSTANCE_OPERATION_NAME_DESC;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.API_VERSION;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.oracle.oci.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.oracle.oci.utils.Outputs.CreateInstanceOutputs.INSTANCE_NAME;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class UpdateInstance {
    @Action(name = UPDATE_INSTANCE_OPERATION_NAME,
            description = UPDATE_INSTANCE_OPERATION_NAME_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = INSTANCE_NAME, description = INSTANCE_NAME_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = Descriptions.Common.SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = Descriptions.Common.FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = TENANCY_OCID, required = true, description = TENANCY_OCID_DESC) String tenancyOcid,
                                       @Param(value = USER_OCID, required = true, description = USER_OCID_DESC) String userOcid,
                                       @Param(value = FINGER_PRINT, encrypted = true, required = true, description = FINGER_PRINT_DESC) String fingerPrint,
                                       @Param(value = PRIVATE_KEY_DATA, encrypted = true, description = PRIVATE_KEY_DATA_DESC) String privateKeyData,
                                       @Param(value = PRIVATE_KEY_FILE, description = PRIVATE_KEY_FILE_DESC) String privateKeyFile,
                                       @Param(value = API_VERSION, description = API_VERSION_DESC) String apiVersion,
                                       @Param(value = REGION, required = true, description = REGION_DESC) String region,
                                       @Param(value = INSTANCE_ID, required = true, description = INSTANCE_ID_DESC) String instanceId,
                                       @Param(value = SHAPE, description = SHAPE_DESC) String shape,
                                       @Param(value = DISPLAY_NAME, description = DISPLAY_NAME_DESC) String displayName,
                                       @Param(value = DEFINED_TAGS, description = DEFINED_TAGS_DESC) String definedTags,
                                       @Param(value = FREEFORM_TAGS, description = FREEFORM_TAGS_DESC) String freeformTags,
                                       @Param(value = OCPUS, description = OCPUS_DESC) String ocpus,
                                       @Param(value = IS_MANAGEMENT_DISABLED, description = IS_MANAGEMENT_DISABLED_DESC) String isManagementDisabled,
                                       @Param(value = IS_MONITORING_DISABLED, description = IS_MONITORING_DISABLED_DESC) String isMonitoringDisabled,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword) {
        apiVersion = defaultIfEmpty(apiVersion, DEFAULT_API_VERSION);
        isManagementDisabled = defaultIfEmpty(isManagementDisabled, EMPTY);
        isMonitoringDisabled = defaultIfEmpty(isMonitoringDisabled, EMPTY);

        definedTags = defaultIfEmpty(definedTags, EMPTY);
        displayName = defaultIfEmpty(displayName, EMPTY);
        freeformTags = defaultIfEmpty(freeformTags, EMPTY);
        ocpus = defaultIfEmpty(ocpus, EMPTY);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);


        final List<String> exceptionMessage = InputsValidation.verifyCommonInputs(privateKeyData, privateKeyFile, proxyPort);
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }

        try {
            final Map<String, String> result =
                    InstanceImpl.updateInstance(OCIUpdateInstanceInputs.builder()
                            .isManagementDisabled(isManagementDisabled)
                            .isMonitoringDisabled(isMonitoringDisabled)
                            .definedTags(definedTags)
                            .displayName(displayName)
                            .freeformTags(freeformTags)
                            .shape(shape)
                            .ocpus(ocpus)
                            .commonInputs(OCICommonInputs.builder()
                                    .tenancyOcid(tenancyOcid)
                                    .userOcid(userOcid)
                                    .fingerPrint(fingerPrint)
                                    .privateKeyData(privateKeyData)
                                    .privateKeyFile(privateKeyFile)
                                    .apiVersion(apiVersion)
                                    .region(region)
                                    .instanceId(instanceId)
                                    .proxyHost(proxyHost)
                                    .proxyPort(proxyPort)
                                    .proxyUsername(proxyUsername)
                                    .proxyPassword(proxyPassword)
                                    .build()).build());
            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = HttpUtils.getOperationResults(result, returnMessage, returnMessage, returnMessage);
            Integer statusCode = Integer.parseInt(result.get(STATUS_CODE));

            if (statusCode >= 200 && statusCode < 300) {
                results.put(INSTANCE_NAME, JsonPath.read(returnMessage, INSTANCE_NAME_JSON_PATH));

            } else {
                return HttpUtils.getFailureResults(instanceId, statusCode, returnMessage);
            }
            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }

    }
}
