/*
 *
 *  * Copyright 2011-2014 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springframework.xd.module;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Factory class to create {@code ModuleDefinition}s.
 *
 * @author Eric Bottard
 */
public class ModuleDefinitions {

	/**
	 * Not meant to be instantiated.
	 */
	private ModuleDefinitions() {

	}


	/**
	 * Create a new definition for a simple module (one where its implementation code can be located). Interpretation
	 * of the location of that code is left other class(es).
	 * @param name the name of the module
	 * @param type the type of the module
	 * @param location where the 'code' for the module can be found
	 */
	public static ModuleDefinition simple(String name, ModuleType type, String location) {
		return new SimpleModuleDefinition(name, type, location);
	}

	/**
	 * Create a new definition for a composed module (one where its behavior comes not from its own 'code', but from
	 * composing pre-existing modules).
	 *
	 * @param name the name of the module
	 * @param type the type of the module
	 * @param dslDefinition the definition that was used to create the module, in Spring XD DSL
	 * @param children information about the child modules that make up the composed module
	 */
	public static ModuleDefinition composed(String name, ModuleType type, String dslDefinition, List<ModuleDefinition> children) {
		return new CompositeModuleDefinition(name, type, dslDefinition, children);
	}

	/**
	 * Create a new definition for a dummy module, for testing purposes only. The resulting module is of the same kind
	 * as returned by {@link #simple(String, ModuleType, String)} but the 'location' must not be relied upon.
	 * @param name the name of the module
	 * @param type the type of the module
	 */
	public static ModuleDefinition dummy(String name, ModuleType type) {
		try {
			File location = File.createTempFile("dummy-module", type + name);
			Assert.isTrue(location.delete(), "could not delete temp file");
			Assert.isTrue(location.mkdirs(), "could not re-create file as a dir");
			location.deleteOnExit();
			return new SimpleModuleDefinition(name, type, "file:" + location.getAbsolutePath());
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
