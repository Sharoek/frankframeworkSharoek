/*
   Copyright 2025 WeAreFrank!

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.frankframework.larva.actions;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.frankframework.core.ListenerException;
import org.frankframework.core.SenderException;
import org.frankframework.core.TimeoutException;
import org.frankframework.stream.Message;

public interface LarvaScenarioAction extends AutoCloseable {

	void executeWrite(Message fileContent, String correlationId, Map<String, Object> xsltParameters) throws TimeoutException, SenderException, ListenerException;

	Message executeRead(Properties properties) throws SenderException, IOException, TimeoutException, ListenerException;
}
