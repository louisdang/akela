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
package com.mozilla.pig.eval.date;

import static java.util.Calendar.DATE;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

import com.mozilla.util.DateUtil;

public class DaysAgo extends EvalFunc<Integer> {

    public static enum ERRORS { DateParseError };
    
    private SimpleDateFormat sdf;
    private long currentDay;
    
    public DaysAgo(String dateFormat) {
        sdf = new SimpleDateFormat(dateFormat);
        currentDay = DateUtil.getTimeAtResolution(System.currentTimeMillis(), DATE);
    }
    
    @Override
    public Integer exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }
        
        Integer daysAgo = null;
        try {
            Date d = sdf.parse((String)input.get(0));
            daysAgo = (int)DateUtil.getTimeDelta(d.getTime(), currentDay, DATE);  
        } catch (ParseException e) {
            pigLogger.warn(this, "Date parsing error", ERRORS.DateParseError);
        }
        
        return daysAgo;
    }

}
