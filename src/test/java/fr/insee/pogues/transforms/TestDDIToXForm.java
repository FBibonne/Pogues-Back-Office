package fr.insee.pogues.transforms;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmlunit.XMLUnitException;
import org.xmlunit.diff.Diff;

import java.io.IOException;

public class TestDDIToXForm {

    private Transformer transformer = new DDIToXFormImpl();

    private XMLDiff xmlDiff = new XMLDiff(transformer);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void simpleDiffTest(){
        performDiffTest("transforms/ddi-to-xform");
    }

    private void performDiffTest(String path) throws XMLUnitException {
        try {
            Diff diff = xmlDiff.getDiff(path);
            Assert.assertFalse(getDiffMessage(diff, path), diff.hasDifferences());
        } catch (IOException e){
            e.printStackTrace();
            Assert.fail();
        } catch (NullPointerException e){
            e.printStackTrace();
            Assert.fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String getDiffMessage(Diff diff, String path) {
        return String.format("Transformed output for %s should match expected DDI document:\n %s", path, diff.toString());
    }
}
