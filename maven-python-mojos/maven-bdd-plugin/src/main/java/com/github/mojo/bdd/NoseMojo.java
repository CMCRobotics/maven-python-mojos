package com.github.mojo.bdd;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * Goat which runs Python Nose/Freshen BDD feature story tests from src/test/python
 * 
 * @goal nose
 * @phase integration-test
 */
public class NoseMojo extends AbstractBddMojo {

	public NoseMojo() {
		super("Nose (with Freshen)","nose.txt","src/test/python","src/test/python","nosetests","--with-freshen","-v");
	}

}