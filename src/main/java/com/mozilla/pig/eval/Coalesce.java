/*
 * Copyright 2012 Mozilla Foundation
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mozilla.pig.eval;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.logicalLayer.schema.Schema.FieldSchema;

public class Coalesce extends EvalFunc<Object> {

    public enum Errors { FRONTEND_EXCEPTION };
    
    @Override
    public Object exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }
        
        Object ret = null;
        for (int i=0; i < input.size(); i++) {
            Object o = input.get(i);
            if (o != null) {
                ret = o;
                break;
            }
        }
        
        return ret;
    }

    public Schema outputSchema(Schema input) {
        if (input == null) {
            return null;
        }

        FieldSchema fs = null;
        try {
            fs = input.getField(0);
        } catch (FrontendException e) {
            pigLogger.warn(this, "Frontend exception getting field schema", Errors.FRONTEND_EXCEPTION);
        }
        
        return new Schema(fs);
    }
}
