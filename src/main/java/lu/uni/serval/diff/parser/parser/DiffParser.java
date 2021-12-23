package lu.uni.serval.diff.parser.parser;

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

import lu.uni.serval.diff.parser.patch.Change;
import lu.uni.serval.diff.parser.patch.Hunk;
import lu.uni.serval.diff.parser.patch.Patch;
import lu.uni.serval.diff.parser.patch.Patches;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiffParser {
    private enum InfoType{
        DIFF,
        DELETED,
        NEW,
        SIMILARITY,
        INDEX,
        COPY,
        RENAME,
        OLD_PATH,
        NEW_PATH,
        CHUNK
    }

    private static final Pattern SIMILARITY_REGEX = Pattern.compile("^(similarity\\sindex\\s)(\\d+)(%)");
    private static final Pattern HUNK_REGEX = Pattern.compile("^@@\\s[+-](\\d+)(,(\\d+))?\\s+\\+(\\d+)(,(\\d+))");

    private String oldPrefix = "a/";
    private String newPrefix = "b/";

    private Patch current = null;
    private Patches patches = null;


    public DiffParser setOldPrefix(String oldPrefix) {
        this.oldPrefix = oldPrefix;
        return this;
    }

    public DiffParser setNewPrefix(String newPrefix) {
        this.newPrefix = newPrefix;
        return this;
    }

    public Patches parse(String diff) throws IOException, MalformedDiffException {
        if(diff == null || diff.isEmpty()){
            return new Patches();
        }

        return parse(new ByteArrayInputStream(diff.getBytes()));
    }

    public Patches parse(InputStream in) throws IOException, MalformedDiffException {
        current = null;
        patches = new Patches();

        if(in == null){
            return patches;
        }

        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line = reader.readLine();
        while(reader.ready()) {
            InfoType infoType = extractInfoType(line);

            switch (infoType){
                case DIFF: line = processDiff(reader); break;
                case INDEX: line = processIndex(reader, line); break;
                case OLD_PATH: line = processOldFilePath(reader, line); break;
                case NEW_PATH: line = processNewFilePath(reader, line); break;
                case DELETED: line = processChangeType(reader, Patch.ChangeType.DELETE); break;
                case NEW: line = processChangeType(reader, Patch.ChangeType.ADD); break;
                case RENAME: line = processChangeType(reader, Patch.ChangeType.RENAME); break;
                case COPY: line = processChangeType(reader, Patch.ChangeType.COPY); break;
                case SIMILARITY: line = processSimilarity(reader, line); break;
                case CHUNK: line = processHunk(reader, line); break;
            }
        }

        return patches;
    }

    private static InfoType extractInfoType(String line) throws MalformedDiffException {
        final String label = line.substring(0, line.indexOf(' '));

        if(label.equals("diff")){
            return InfoType.DIFF;
        }
        if(label.equals("index")){
            return InfoType.INDEX;
        }
        if(label.equals("deleted")){
            return InfoType.DELETED;
        }
        if(label.equals("new")){
            return InfoType.NEW;
        }
        if(label.equals("similarity")){
            return InfoType.SIMILARITY;
        }
        if(label.equals("copy")){
            return InfoType.COPY;
        }
        if(label.equals("rename")){
            return InfoType.RENAME;
        }
        if(label.equals("---")){
            return InfoType.OLD_PATH;
        }
        if(label.equals("+++")){
            return InfoType.NEW_PATH;
        }
        if(label.equals("@@")){
            return InfoType.CHUNK;
        }

        throw new MalformedDiffException("could not parse line: " + line);
    }

    private String processDiff(BufferedReader reader) throws IOException {
        current = new Patch();
        patches.add(current);

        return reader.readLine();
    }

    private String processIndex(BufferedReader reader, String line) throws IOException {
        final String[] versions = line.split("\\s")[1].split("\\.\\.");

        current.setOldVersion(versions[0]);
        current.setNewVersion(versions[1]);

        return reader.readLine();
    }

    private String processOldFilePath(BufferedReader reader, String line) throws IOException {
        current.setOldFile(line.split("\\s")[1].replaceAll("^" + oldPrefix, ""));
        return reader.readLine();
    }

    private String processNewFilePath(BufferedReader reader, String line) throws IOException {
        current.setNewFile(line.split("\\s")[1].replaceAll("^" + newPrefix, ""));
        return reader.readLine();
    }

    private String processChangeType(BufferedReader reader, Patch.ChangeType changeType) throws IOException {
        current.setChangeType(changeType);
        return reader.readLine();
    }

    private String processSimilarity(BufferedReader reader, String line) throws MalformedDiffException, IOException {
        final Matcher matcher = SIMILARITY_REGEX.matcher(line);

        if(!matcher.matches()){
            throw new MalformedDiffException("failed to parse similarity definition: " + line);
        }

        final int similarity = Integer.parseInt(matcher.group(2));
        current.setSimilarity(similarity);

        return reader.readLine();
    }

    private String processHunk(BufferedReader reader, String line) throws IOException, MalformedDiffException {
        final Matcher matcher = HUNK_REGEX.matcher(line);

        if(!matcher.find()){
            throw new MalformedDiffException("failed to parse hunk definition: " + line);
        }

        int oldPosition = Integer.parseInt(matcher.group(1));
        int newPosition = Integer.parseInt(matcher.group(4));

        final Hunk hunk = new Hunk();

        String content = "";
        while(reader.ready()) {
            content = reader.readLine();
            final char sign = content.isEmpty() ? ' ' : content.charAt(0);

            if(!isHunkBlock(sign)){
                break;
            }
            else if(sign == '+'){
                hunk.addChange(new Change(Change.Type.ADD, newPosition++, content.substring(1)));
            }
            else if(sign == '-'){
                hunk.addChange(new Change(Change.Type.REMOVE, oldPosition++, content.substring(1)));
            }
            else{
                ++newPosition;
                ++oldPosition;
            }
        }

        current.addHunk(hunk);

        return content;
    }

    private boolean isHunkBlock(char sign){
        return sign == ' ' || sign == '+' || sign == '-';
    }
}
