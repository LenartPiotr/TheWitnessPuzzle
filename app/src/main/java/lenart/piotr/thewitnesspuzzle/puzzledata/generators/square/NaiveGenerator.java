package lenart.piotr.thewitnesspuzzle.puzzledata.generators.square;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzleSectorsBuilder;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.IGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.IPathGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.Solution;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.ISectorsComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Sector;

public class NaiveGenerator implements IGenerator {
    int width;
    int height;
    List<ComponentData> components;
    int[] percents;
    IPathGenerator pathGenerator;

    SquarePuzzle puzzle;
    SquarePuzzleSectorsBuilder sectorsBuilder;
    Random random;

    NaiveGenerator() { }

    @Override
    public Solution generate() throws WrongComponentException {
        random = new Random();
        puzzle = SquarePuzzle.createEmpty(width, height);
        IPath ipath = pathGenerator.generate(puzzle);
        if (!(ipath instanceof Path)) throw new WrongComponentException(this, Path.class, ipath);
        Path path = (Path) ipath;
        puzzle.getStartPoints().add(path.steps.get(0).clone());
        puzzle.getEndPoints().add(path.steps.get(path.steps.size() - 1).clone());
        sectorsBuilder = new SquarePuzzleSectorsBuilder(puzzle, path);
        sectorsBuilder.setRandom(random);
        Collections.shuffle(components);
        List<IComponent> randomComponents = components.stream().filter(c -> c.percent == 0).map(c -> c.component).collect(Collectors.toList());
        for (int i = 0; i < percents.length; i++) {
            IComponent c = randomComponents.get(i);
            setUpComponent(c, path, percents[i]);
        }
        for (ComponentData componentData : components) {
            if (componentData.percent == 0) continue;
            IComponent c = componentData.component;
            setUpComponent(c, path, componentData.percent);
        }
        puzzle.registerComponents();
        return new Solution(puzzle, ipath);
    }

    void setUpComponent(IComponent component, Path path, int percent) {
        puzzle.getComponents().add(component);
        component.reset();
        if (component instanceof ISectorsComponent) ((ISectorsComponent) component).setSectorsBuilder(sectorsBuilder);
        component.setRandom(random);
        component.addRandomElement(puzzle, path, percent);
    }

    public static class Builder {
        int width = 5;
        int height = 5;
        List<ComponentData> components = new ArrayList<>();
        IPathGenerator pathGenerator = new DfsPathGenerator();
        int[] percentage = new int[0];

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }
        public Builder addRandomComponent(IComponent component) {
            if (component == null) return this;
            if (components.stream().anyMatch(component1 -> component1.component.getClass().equals(component.getClass()))) return this;
            components.add(new ComponentData(component, 0));
            return this;
        }
        public Builder addComponent(IComponent component, int fillPercent) {
            if (component == null) return this;
            if (components.stream().anyMatch(component1 -> component1.component.getClass().equals(component.getClass()))) return this;
            components.add(new ComponentData(component, fillPercent));
            return this;
        }
        public Builder setPathGenerator(IPathGenerator generator) {
            this.pathGenerator = generator;
            return this;
        }
        public Builder setComponentsRandomPercentage(int[] percentage) {
            this.percentage = percentage;
            return this;
        }
        public NaiveGenerator build() {
            NaiveGenerator generator = new NaiveGenerator();
            generator.width = width;
            generator.height = height;
            generator.components = components;
            generator.pathGenerator = pathGenerator;
            generator.percents = percentage;
            return generator;
        }
    }

    static class ComponentData {
        IComponent component;
        int percent;
        ComponentData(IComponent component, int percent) {
            this.component = component;
            this.percent = percent;
        }
    }
}
