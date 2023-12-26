package lenart.piotr.thewitnesspuzzle.puzzledata.generators;

import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;

public interface IGenerator {
    Solution generate() throws WrongComponentException;
}
