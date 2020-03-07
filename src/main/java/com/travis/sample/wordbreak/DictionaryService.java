package com.travis.sample.wordbreak;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author travis chen
 * @date 2020/3/8
 * <p>
 * desc：
 */
public class DictionaryService {

    private Set<String> dict;


    public void initDict(String... words) {
        this.dict = new HashSet<>();
        dict.addAll(Arrays.asList(words));
    }

    /**
     * construct a dict index
     * key = word without space
     * value = relative words
     *
     * @param customizedDict
     * @param dictType
     * @return
     */
    public Map<String, Set<String>> contructDicIndex(Set<String> customizedDict, DictTypeEnum dictType) {
        Set<String> dictTmp = new HashSet<>();
        switch (dictType) {
            case DEFAULT:
                dictTmp = dict;
                break;
            case CUSTOMIZED:
                dictTmp = customizedDict;
                break;
            case COMBINED:
                dictTmp.addAll(dict);
                dictTmp.addAll(customizedDict);
                break;
            default:
        }

        Map<String, Set<String>> dictIndex = new HashMap<>();
        for (String s : dictTmp) {
            Set<String> relativeWords = dictIndex.computeIfAbsent(s.replaceAll(StringUtils.SPACE, StringUtils.EMPTY),
                    k -> new HashSet<>());
            relativeWords.add(s);
        }
        return dictIndex;
    }

}