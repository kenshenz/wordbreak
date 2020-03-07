package com.travis.sample.wordbreak;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author travis chen
 * @date 2020/3/8
 * <p>
 * desc：
 */
public class WordBreakServiceTest {

    private WordBreakService wordBreakService;

    @Before
    public void before() {
        wordBreakService = new WordBreakService();
        DictionaryService dictionaryService = new DictionaryService();
        dictionaryService.initDict("i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "man go");
        wordBreakService.setDictionaryService(dictionaryService);
    }

    @Test
    public void testBreakWordWithDefaultDict() {
        String sentence = "ilikesamsungmobile";

        Set<String> result = wordBreakService.breakWord(sentence, null, DictTypeEnum.DEFAULT);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 2);
    }

    @Test
    public void testBreakWordWithCustomizedDict() {
        Set<String> customizedDict = new HashSet<>(Arrays.asList(new String[]{"i", "like", "sam", "sung", "mobile",
                "icecream", "man go", "mango"}));
        String sentence = "ilikesamsungmobile";

        Set<String> result = wordBreakService.breakWord(sentence, customizedDict, DictTypeEnum.CUSTOMIZED);


        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 1);
    }

    @Test
    public void testBreakWordWithCombinedDict() {
        Set<String> customizedDict = new HashSet<>(Arrays.asList(new String[]{"i", "like", "sam", "sung", "mobile",
                "icecream", "man go", "mango"}));
        String sentence = "ilikesamsungmobile";

        Set<String> result = wordBreakService.breakWord(sentence, customizedDict, DictTypeEnum.COMBINED);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 2);
    }

    @Test
    public void testBreakWordWithUnknownWord() {
        Set<String> customizedDict = new HashSet<>(Arrays.asList(new String[]{"i", "like", "sam", "sung", "mobile",
                "icecream", "man go", "mango"}));
        String sentence = "ilikeicecreamandmango";

        Set<String> result = wordBreakService.breakWord(sentence, customizedDict, DictTypeEnum.COMBINED);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 6);
    }

    @Test
    public void testBreakWordWithUnknownWordAtEnd() {
        Set<String> customizedDict = new HashSet<>(Arrays.asList(new String[]{"i", "like", "sam", "sung", "mobile",
                "icecream", "man go", "mango"}));
        String sentence = "ilikeicecreamandmangoverymuch";

        Set<String> result = wordBreakService.breakWord(sentence, customizedDict, DictTypeEnum.COMBINED);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 6);
    }
}