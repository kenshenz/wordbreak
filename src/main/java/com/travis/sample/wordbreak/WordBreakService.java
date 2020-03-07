package com.travis.sample.wordbreak;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author travis chen
 * @date 2020/3/7
 * <p>
 * desc：
 */
public class WordBreakService {

    private static Logger log = LoggerFactory.getLogger(WordBreakService.class);


    private DictionaryService dictionaryService;

    /**
     * break sentence which without any spaces between the words, break by default dict and/or customized dict
     *
     * @param sentence       sentence for breaking
     * @param customizedDict customized dict
     * @param dictType       dict type
     * @return
     * @see DictTypeEnum
     */
    public Set<String> breakWord(String sentence, Set<String> customizedDict, DictTypeEnum dictType) {
        log.info("breakWord start, sentence={}, customizedDict={}, dictType={}", sentence, customizedDict, dictType);
        Map<String, Set<String>> dictIndex = dictionaryService.contructDicIndex(customizedDict, dictType);
        Set<String> dp[] = this.constructDpArray(sentence, dictIndex.keySet());
        Set<String> result = new HashSet<>();
        output(dictIndex, dp, dp.length - 1, StringUtils.EMPTY, result);
        log.info("breakWord end, dictIdx={}, dp={}, result={}", dictIndex, Arrays.toString(dp), result);
        return result;
    }

    /**
     * output the formatted sentences
     *
     * @param dictIndex  dict map, key = word without any space, value = corresponding words exists in dict
     * @param dp         array which record the dictionary word and its index
     * @param processIdx
     * @param strTmp
     * @param result
     */
    private void output(Map<String, Set<String>> dictIndex, Set<String>[] dp, int processIdx, String strTmp,
                        Set<String> result) {
        if (processIdx == 0) {
            result.add(strTmp.trim());
            return;
        }

        dp[processIdx].forEach(word -> {
            Set<String> relativeWords = dictIndex.get(word);
            Set<String> jointWords = new HashSet<>();
            if (CollectionUtils.isNotEmpty(relativeWords)) {
                relativeWords.forEach(correspondingWord -> jointWords.add(correspondingWord + StringUtils.SPACE + strTmp));
            } else {
                jointWords.add(word + StringUtils.SPACE + strTmp);
            }

            jointWords.forEach(jointWord -> output(dictIndex, dp, processIdx - word.length(), jointWord, result));
        });
    }

    /**
     * create an array, store the word we found when loop the sentence
     *
     * @param sentence
     * @param dict
     * @return
     */
    private Set<String>[] constructDpArray(String sentence, Set<String> dict) {
        Set<String> dp[] = new HashSet[sentence.length() + 1];
        dp[0] = new HashSet<>();
        // exists in sentence but not in dictionary
        StringBuilder unknownWord = new StringBuilder();
        for (int i = 0, sLen = sentence.length(); i < sLen; i++) {
            if (dp[i] == null) {
                continue;
            }
            boolean processFlag = false;
            for (String word : dict) {
                int endIdx = i + word.length();
                if (endIdx > sLen) {
                    continue;
                }
                if (sentence.substring(i, endIdx).equals(word)) {
                    if (dp[endIdx] == null) {
                        dp[endIdx] = new HashSet<>();
                    }
                    dp[endIdx].add(word);
                    processFlag = true;
                }
            }
            if (!processFlag) {
                unknownWord.append(sentence.charAt(i));
                if (dp[i + 1] == null) {
                    dp[i + 1] = new HashSet<>();
                }
                if (i == sLen - 1) {
                    dp[i + 1].add(unknownWord.toString());
                }
            } else if (!StringUtils.EMPTY.equals(unknownWord.toString())) {
                dp[i].add(unknownWord.toString());
                unknownWord.setLength(0);
            }
        }

        return dp;
    }

    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }
}
