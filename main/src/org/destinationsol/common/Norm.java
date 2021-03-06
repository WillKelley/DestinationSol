/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.destinationsol.common;

import java.lang.annotation.Documented;

/**
 * When used for annotating methods, indicates that the returned float value is an angle and is normalized (-180 < a && a <= 180)
 * When used for annotating params, indicates that the param angle must be normalized
 */
@Documented
public @interface Norm {

}
