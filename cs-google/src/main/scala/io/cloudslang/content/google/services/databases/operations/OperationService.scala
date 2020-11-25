
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

package io.cloudslang.content.google.services.databases.operations

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sqladmin.model.Operation
import io.cloudslang.content.google.services.databases.DatabaseService

object OperationService {
  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String,
          operationName: String): Operation =
    DatabaseService.OperationsService(httpTransport, jsonFactory, credential).get(project, operationName).execute()

}