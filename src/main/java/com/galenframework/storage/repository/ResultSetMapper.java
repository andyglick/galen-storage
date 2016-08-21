/*******************************************************************************
* Copyright 2016 Ivan Shubin http://galenframework.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package com.galenframework.storage.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class ResultSetMapper {
    private final ResultSet resultSet;

    ResultSetMapper(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    interface RSFunction<T> {
        T apply(ResultSet rs) throws SQLException;
    }

    <T> Optional<T> single(RSFunction<T> mapFunction) {
        try {
            if (resultSet.next()) {
                return Optional.ofNullable(mapFunction.apply(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    <T> List<T> list(RSFunction<T> mapFunction) {
        try {
            List<T> list = new LinkedList<T>();
            while (resultSet.next()) {
                list.add(mapFunction.apply(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
