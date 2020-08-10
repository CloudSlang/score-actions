/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.maps.services;

import io.cloudslang.content.maps.entities.AddKeyInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.serialization.MapDeserializer;
import io.cloudslang.content.maps.serialization.MapSerializer;
import io.cloudslang.content.maps.validators.AddKeyInputValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AddKeyService {

    private final AddKeyInputValidator validator = new AddKeyInputValidator();
    private final MapDeserializer deserializer = new MapDeserializer();
    private final MapSerializer serializer = new MapSerializer();

    public @NotNull Map<String, String> execute(@NotNull AddKeyInput input) throws Exception {
        ValidationException validationEx = validator.validate(input);
        if(validationEx != null) {
            throw validationEx;
        }

        Map<String, String> map = deserializer.deserialize(input.getMap());
        map.put(input.getKey(), input.getValue());
        String returnResult = serializer.serialize(map);

        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
