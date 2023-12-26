package lenart.piotr.thewitnesspuzzle.puzzledata.generators.square;

import java.util.ArrayList;
import java.util.List;

import lenart.piotr.thewitnesspuzzle.puzzledata.components.IComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.IGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.IPathGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.Solution;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;

public class NaiveGenerator implements IGenerator {
    int width;
    int height;
    List<IComponent> components;
    IPathGenerator pathGenerator;
    NaiveGenerator() { }

    @Override
    public Solution generate() throws WrongComponentException {
        SquarePuzzle puzzle = SquarePuzzle.createEmpty(width, height);
        IPath path = pathGenerator.generate(puzzle);
        return new Solution(puzzle, path);
    }

    public static class Builder {
        int width = 5;
        int height = 5;
        List<IComponent> components = new ArrayList<>();
        IPathGenerator pathGenerator = new BouncingPathGenerator();

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }
        public Builder addComponent(IComponent component) {
            if (component == null) return this;
            if (components.stream().anyMatch(component1 -> component1.getClass().equals(component.getClass()))) return this;
            components.add(component);
            return this;
        }
        public Builder setPathGenerator(IPathGenerator generator) {
            this.pathGenerator = generator;
            return this;
        }
        public NaiveGenerator build() {
            NaiveGenerator generator = new NaiveGenerator();
            generator.width = width;
            generator.height = height;
            generator.components = components;
            generator.pathGenerator = pathGenerator;
            return generator;
        }
    }
}
