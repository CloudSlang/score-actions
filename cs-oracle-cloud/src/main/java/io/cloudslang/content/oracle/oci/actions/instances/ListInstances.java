/*
 * (c) Copyright 2020 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import io.cloudslang.content.oracle.oci.entities.inputs.OCIInstanceInputs;
import io.cloudslang.content.oracle.oci.services.models.instances.InstanceImpl;
import io.cloudslang.content.oracle.oci.utils.*;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.join;

public class ListInstances {

    @Action(name =Constants.ListInstancesConstants.LIST_INSTANCES_OPERATION_NAME ,
            description = Descriptions.ListInstances.LIST_INSTANCES_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = Descriptions.Common.RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = Descriptions.Common.EXCEPTION_DESC),
                    @Output(value = Outputs.ListInstancesOutputs.INSTANCE_LIST, description = Descriptions.ListInstances.INSTANCE_LIST_DESC),
                    @Output(value = Constants.Common.STATUS_CODE, description = Descriptions.Common.STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = Descriptions.Common.SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = Descriptions.Common.FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = Inputs.CommonInputs.TENANCY_OCID, required = true, description = Descriptions.Common.TENANCY_OCID_DESC) String tenancyOcid,
                                       @Param(value = Inputs.CommonInputs.USER_OCID, required = true, description = Descriptions.Common.USER_OCID_DESC) String userOcid,
                                       @Param(value = Inputs.CommonInputs.FINGER_PRINT, encrypted = true, required = true, description = Descriptions.Common.FINGER_PRINT_DESC) String fingerPrint,
                                       @Param(value = Inputs.CommonInputs.PRIVATE_KEY_FILE, required = true, description = Descriptions.Common.PRIVATE_KEY_FILE_DESC) String privateKeyFile,
                                       @Param(value = Inputs.ListInstancesInputs.COMPARTMENT_OCID, required = true, description = Descriptions.ListInstances.COMPARTMENT_OCID_DESC) String compartmentOcid,
                                       @Param(value = Inputs.CommonInputs.API_VERSION, description = Descriptions.Common.API_VERSION_DESC) String apiVersion,
                                       @Param(value = Inputs.CommonInputs.REGION, required = true, description = Descriptions.Common.REGION_DESC) String region,
                                       @Param(value = Inputs.CommonInputs.PROXY_HOST, description = Descriptions.Common.PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = Inputs.CommonInputs.PROXY_PORT, description = Descriptions.Common.PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = Inputs.CommonInputs.PROXY_USERNAME, description = Descriptions.Common.PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = Inputs.CommonInputs.PROXY_PASSWORD, encrypted = true, description = Descriptions.Common.PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = Descriptions.Common.TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = Descriptions.Common.X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = Descriptions.Common.TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = Descriptions.Common.TRUST_PASSWORD_DESC) String trustPassword,
                                       @Param(value = KEYSTORE, description = Descriptions.Common.KEYSTORE_DESC) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true, description = Descriptions.Common.KEYSTORE_PASSWORD_DESC) String keystorePassword,
                                       @Param(value = CONNECT_TIMEOUT, description = Descriptions.Common.CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = Descriptions.Common.SOCKET_TIMEOUT_DESC) String socketTimeout,
                                       @Param(value = KEEP_ALIVE, description = Descriptions.Common.KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = Descriptions.Common.CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = Descriptions.Common.CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = Descriptions.Common.RESPONSE_CHARACTER_SET_DESC) String responseCharacterSet){
        apiVersion = defaultIfEmpty(apiVersion,Constants.Common.DEFAULT_API_VERSION );
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, Constants.Common.DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, Constants.Common.BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, Constants.Common.STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, Constants.Common.DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, Constants.Common.CHANGEIT);
        keystore = defaultIfEmpty(keystore, Constants.Common.DEFAULT_JAVA_KEYSTORE);
        keystorePassword = defaultIfEmpty(keystorePassword, Constants.Common.CHANGEIT);
        connectTimeout = defaultIfEmpty(connectTimeout, Constants.Common.CONNECT_TIMEOUT_CONST);
        socketTimeout = defaultIfEmpty(socketTimeout, Constants.Common.ZERO);
        keepAlive = defaultIfEmpty(keepAlive, Constants.Common.BOOLEAN_TRUE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, Constants.Common.CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, Constants.Common.CONNECTIONS_MAX_TOTAL_CONST);
        responseCharacterSet = defaultIfEmpty(responseCharacterSet, Constants.Common.UTF8);
        final List<String> exceptionMessage = InputsValidation.verifyCommonInputs(proxyPort, trustAllRoots,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, Constants.Common.NEW_LINE));
        }

        try {
            final Map<String, String> result =
            InstanceImpl.listInstances(OCIInstanceInputs.builder()
                    .compartmentOcid(compartmentOcid)
                    .commonInputs(OCICommonInputs.builder()
                            .tenancyOcid(tenancyOcid)
                            .userOcid(userOcid)
                            .fingerPrint(fingerPrint)
                            .privateKeyFilename(privateKeyFile)
                            .apiVersion(apiVersion)
                            .region(region)
                            .proxyHost(proxyHost)
                            .proxyPort(proxyPort)
                            .proxyUsername(proxyUsername)
                            .proxyPassword(proxyPassword)
                            .trustAllRoots(trustAllRoots)
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .keystore(keystore)
                            .keystorePassword(keystorePassword)
                            .connectTimeout(connectTimeout)
                            .socketTimeout(socketTimeout)
                            .keepAlive(keepAlive)
                            .connectionsMaxPerRoot(connectionsMaxPerRoute)
                            .connectionsMaxTotal(connectionsMaxTotal)
                            .responseCharacterSet(responseCharacterSet)
                            .build())
                    .build());
            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = HttpUtils.getOperationResults(result, returnMessage, returnMessage, returnMessage);
            Integer statusCode = Integer.parseInt(result.get(Constants.Common.STATUS_CODE));

            if (statusCode >= 200 && statusCode < 300) {
                final List<String> instance_list = JsonPath.read(returnMessage, Constants.ListInstancesConstants.INSTANCES_LIST_JSON_PATH);
                if (!instance_list.isEmpty()) {
                    final String instanceListAsString = StringUtils.join(instance_list.toArray(), Constants.Common.DELIMITER);
                    results.put(Outputs.ListInstancesOutputs.INSTANCE_LIST, instanceListAsString);
                } else {
                    results.put(Outputs.ListInstancesOutputs.INSTANCE_LIST, EMPTY);
                }

            }else{
                return HttpUtils.getFailureResults(compartmentOcid,statusCode,returnMessage);
            }
            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }

    }
    }

