package lenart.piotr.thewitnesspuzzle.puzzledata.generators.square;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.IGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.IPathGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.Solution;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Sector;

public class NaiveGenerator implements IGenerator {
    int width;
    int height;
    List<IComponent> components;
    IPathGenerator pathGenerator;
    NaiveGenerator() { }

    @Override
    public Solution generate() throws WrongComponentException {
        SquarePuzzle puzzle = SquarePuzzle.createEmpty(width, height);
        IPath ipath = pathGenerator.generate(puzzle);
        if (!(ipath instanceof Path)) throw new WrongComponentException(this, Path.class, ipath);
        Path path = (Path) ipath;
        puzzle.getStartPoints().add(path.steps.get(0).clone());
        puzzle.getEndPoints().add(path.steps.get(path.steps.size() - 1).clone());
        List<Sector> sectors = Sector.getSectors(width, height, path);
        for (IComponent c : components) {
            puzzle.getComponents().add(c);
            c.reset();
            c.addRandomElement(puzzle, path, sectors, 30);
        }
        puzzle.registerComponents();
        return new Solution(puzzle, ipath);
    }

    public static class Builder {
        int width = 5;
        int height = 5;
        List<IComponent> components = new ArrayList<>();
        IPathGenerator pathGenerator = new DfsPathGenerator();

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
