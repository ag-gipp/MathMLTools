package com.formulasearchengine.mathmltools.similarity.node;

import com.formulasearchengine.mathmltools.helper.CMMLHelper;
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Vincent Stange
 */
public class MathNodeGeneratorTest {

    @Test
    public void generateMathNode_normal() throws Exception {
        // mathml from latexml and just get the cmml semantics
        testConverterOnFile(CMMLHelper::getFirstApplyNode, "mathnode1", false);
    }

    @Test
    public void generateMathNode_strict() throws Exception {
        // mathml from latexml and get the strict cmml semantics
        testConverterOnFile(CMMLHelper::getStrictCmml, "mathnode2", true);
    }

    @Test
    public void generateMathNode_mathoid_normal() throws Exception {
        // mathml from mathoid and get the cmml semantics
        testConverterOnFile(CMMLHelper::getFirstApplyNode, "mathnode3", false);
    }

    @Test
    public void generateMathNode_mathoid_strict() throws Exception {
        // mathml from mathoid and get the strict cmml semantics
        testConverterOnFile(CMMLHelper::getStrictCmml, "mathnode4", true);
    }
    @Test
    public void generateMathNodeMathoidStrictAny() throws Exception {
        // mathml from mathoid and get the strict cmml semantics
        testConverterOnFile(CMMLHelper::getFirstNode, "mathnode4", true);
    }

    @Test
    public void generateMathNodeHyplag() throws Exception {
        // mathml from hyplag and get the strict cmml semantics
        testConverterOnFile(CMMLHelper::getFirstNode, "mathnode5", true);
    }

    private void testConverterOnFile(Function<String, Node> convert, String basicFilename, boolean checkAbstract) throws Exception {
        String expected = IOUtils.toString(this.getClass().getResourceAsStream(basicFilename + "_expected.txt"), "UTF-8");
        String mathml = IOUtils.toString(this.getClass().getResourceAsStream(basicFilename + "_test.txt"), "UTF-8");
        final Node applyRoot = convert.apply(mathml);
        MathNode mathNode = MathNodeGenerator.generateMathNode(applyRoot);

        if (checkAbstract) {
            mathNode = MathNodeGenerator.toAbstract(mathNode);
        }

        assertThat(MathNodeGenerator.printMathNode(mathNode, ""), CoreMatchers.equalTo(expected));
    }

}
