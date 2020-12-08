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
package io.cloudslang.content.filesystem.utils;

public class Descriptions {

    public static class Common {
        public static final String RETURN_CODE_DESCRIPTION = "0 if the operation succeeded, -1 otherwise.";
        public static final String EXCEPTION_DESCRIPTION = "The exception's stack trace if the operation failed. Empty otherwise.";
    }

    public static class GetSize {
        public static final String SOURCE_DESCRIPTION = "The file to read. It must be an absolute path.";
        public static final String THRESHOLD_DESCRIPTION = "The threshold to compare the file size to (in bytes).";

        public static final String GET_SIZE_RETURN_RESULT_DESCRIPTION = "The file's size in bytes if the operation succeeded. " +
                "Otherwise it will contain the message of the exception.";
        public static final String SIZE_DESCRIPTION = "The file's size in bytes.";
        public static final String LESS_THAN_DESCRIPTION = "The file's size is smaller than the threshold.";
        public static final String EQUAL_TO_DESCRIPTION = "The file's size is the same as the threshold.";
        public static final String GREATER_THAN_DESCRIPTION = "The file's size is the greater than the threshold.";
        public static final String FAILURE_DESCRIPTION = "The operation failed.";

    }
}
