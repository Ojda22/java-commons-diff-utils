package lu.uni.serval.diff.parser.utils;

/*-
 * #%L
 * Commons Diff Utils
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
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
 * #L%
 */

import lu.uni.serval.diff.parser.parser.DiffParser;
import lu.uni.serval.diff.parser.parser.MalformedDiffException;
import lu.uni.serval.diff.parser.patch.Change;
import lu.uni.serval.diff.parser.patch.Patch;
import lu.uni.serval.diff.parser.patch.Patches;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StackTraceMapper {
    private StackTraceMapper() {}

    public static String map(String newStackTrace, String diff){
        try {
            final Patches patches = new DiffParser().parse(diff);
            final List<String> stack = Arrays.stream(newStackTrace.split(";"))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            return String.join(";", map(stack, patches));
        } catch (IOException | MalformedDiffException e) {
            return "";
        }
    }

    private static List<String> map(List<String> stack, Patches patches){
        return stack.stream()
                .map(s -> computeLine(s, patches))
                .collect(Collectors.toList());
    }

    private static String computeLine(String old, Patches patches){
        if(old.trim().isEmpty()){
            return old;
        }

        final String[] split = old.split(":");

        final String file = split[0].replace('.', '/') + ".java";
        final int line = Integer.parseInt(split[2]);

        final Optional<Patch> patch = patches.getByNewFile(file, false);

        if(!patch.isPresent()){
            return old;
        }

        final long add = countPositions(patch.get(), line, Change.Type.ADD);
        final long remove = countPositions(patch.get(), line, Change.Type.REMOVE);

        final long newLine = line - add + remove;

        return split[0] + ":" + split[1] + ":" + newLine;
    }

    private static long countPositions(Patch patch, int line, Change.Type type){
        return patch.getHunks().stream()
                .flatMap(h -> h.getChanges().stream())
                .filter(c -> c.getType() == type)
                .map(Change::getPosition)
                .filter(p -> p < line)
                .count();
    }
}
