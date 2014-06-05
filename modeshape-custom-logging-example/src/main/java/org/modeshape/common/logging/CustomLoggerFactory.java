/*
 * ModeShape (http://www.modeshape.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.modeshape.common.logging;

/**
 * To create a custom ModeShape logger, create a class in the <code>org.modeshape.common.logging</code> package that is named
 * {@link CustomLoggerFactory} and extends {@link LogFactory}. It should create your own implementation of {@link Logger}.
 */
public class CustomLoggerFactory extends LogFactory {

    @Override
    protected Logger getLogger( String name ) {
        return new CustomLogger(name);
    }

}
