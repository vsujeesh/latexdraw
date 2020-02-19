package net.sf.latexdraw.command.shape;

import io.github.interacto.jfx.test.UndoableCmdTest;
import java.util.stream.Stream;
import net.sf.latexdraw.model.ShapeFactory;
import net.sf.latexdraw.model.api.shape.Drawing;
import net.sf.latexdraw.model.api.shape.Group;
import net.sf.latexdraw.model.api.shape.Position;
import net.sf.latexdraw.model.api.shape.Rectangle;
import net.sf.latexdraw.service.PreferencesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the command ScaleShapes. Generated by Interacto test-gen.
 */
@Tag("command")
class ScaleShapesTest extends UndoableCmdTest<ScaleShapes> {
	Position refPosition;
	double newX;
	double newY;
	Drawing drawing;
	Group shape;
	Rectangle s0;

	@BeforeEach
	void setUp() {
		bundle = new PreferencesService().getBundle();
	}

	@Override
	protected Stream<Runnable> canDoFixtures() {
		return Stream.of(Position.values()).map(pos -> () -> {
			refPosition = pos;
			cmd = new ScaleShapes(shape, drawing, refPosition);
			switch(pos) {
				case NORTH:
					newX = 0;
					newY = 65;
					break;
				case SOUTH:
					newX = 0;
					newY = 200;
					break;
				case EAST:
					newX = 55;
					newY = 0;
					break;
				case WEST:
					newX = 190;
					newY = 0;
					break;
				case NE:
					newX = 100;
					newY = 65;
					break;
				case NW:
					newX = 120;
					newY = 70;
					break;
				case SE:
					newX = 110;
					newY = 165;
					break;
				case SW:
					newX = 100;
					newY = 200;
					break;
			}
			cmd.setNewX(newX);
			cmd.setNewY(newY);
		});
	}

	@Override
	protected void commonCanDoFixture() {
		s0 = ShapeFactory.INST.createRectangle(ShapeFactory.INST.createPoint(20, 60), 100, 200);
		shape = ShapeFactory.INST.createGroup(s0);
		drawing = ShapeFactory.INST.createDrawing();
	}

	@Override
	protected Stream<Runnable> cannotDoFixtures() {
		return Stream.of(
			() -> cmd = new ScaleShapes(ShapeFactory.INST.createGroup(), ShapeFactory.INST.createDrawing(), Position.EAST),
			() -> cmd = new ScaleShapes(ShapeFactory.INST.createGroup(), ShapeFactory.INST.createDrawing(), Position.WEST),
			() -> cmd = new ScaleShapes(ShapeFactory.INST.createGroup(), ShapeFactory.INST.createDrawing(), Position.NORTH),
			() -> cmd = new ScaleShapes(ShapeFactory.INST.createGroup(), ShapeFactory.INST.createDrawing(), Position.SOUTH),
			() -> {
				commonCanDoFixture();
				cmd = new ScaleShapes(shape, drawing, Position.SE);
				cmd.setNewX(110);
			},
			() -> {
				commonCanDoFixture();
				cmd = new ScaleShapes(shape, drawing, Position.NE);
				cmd.setNewY(65);
			});
	}

	@Override
	protected Stream<Runnable> doCheckers() {
		return Stream.of(() -> {
			assertThat(drawing.isModified()).isTrue();
			assertThat(cmd.hadEffect()).isTrue();

			switch(refPosition) {
				case NORTH:
					assertThat(s0.getTopLeftPoint()).isEqualTo(ShapeFactory.INST.createPoint(20, 60));
					assertThat(s0.getBottomRightPoint()).isEqualTo(ShapeFactory.INST.createPoint(120, 65));
					break;
				case SOUTH:
					assertThat(s0.getTopLeftPoint()).isEqualTo(ShapeFactory.INST.createPoint(20, 200));
					assertThat(s0.getBottomRightPoint()).isEqualTo(ShapeFactory.INST.createPoint(120, 260));
					break;
				case EAST:
					assertThat(s0.getTopLeftPoint()).isEqualTo(ShapeFactory.INST.createPoint(55, 60));
					assertThat(s0.getBottomRightPoint()).isEqualTo(ShapeFactory.INST.createPoint(120, 260));
					break;
				case WEST:
					assertThat(s0.getTopLeftPoint()).isEqualTo(ShapeFactory.INST.createPoint(20, 60));
					assertThat(s0.getBottomRightPoint()).isEqualTo(ShapeFactory.INST.createPoint(190, 260));
					break;
				case NE:
					assertThat(s0.getTopLeftPoint()).isEqualTo(ShapeFactory.INST.createPoint(100, 60));
					assertThat(s0.getBottomRightPoint()).isEqualTo(ShapeFactory.INST.createPoint(120, 65));
					break;
				case NW:
					assertThat(s0.getTopLeftPoint()).isEqualTo(ShapeFactory.INST.createPoint(20, 60));
					assertThat(s0.getBottomRightPoint()).isEqualTo(ShapeFactory.INST.createPoint(120, 70));
					break;
				case SE:
					assertThat(s0.getTopLeftPoint()).isEqualTo(ShapeFactory.INST.createPoint(110, 165));
					assertThat(s0.getBottomRightPoint()).isEqualTo(ShapeFactory.INST.createPoint(120, 260));
					break;
				case SW:
					assertThat(s0.getTopLeftPoint()).isEqualTo(ShapeFactory.INST.createPoint(20, 200));
					assertThat(s0.getBottomRightPoint()).isEqualTo(ShapeFactory.INST.createPoint(100, 260));
					break;
			}
		});
	}

	@Override
	protected Stream<Runnable> undoCheckers() {
		return Stream.of(() -> {
			assertThat(drawing.isModified()).isFalse();
			assertThat(s0.getTopLeftPoint()).isEqualTo(ShapeFactory.INST.createPoint(20, 60));
			assertThat(s0.getBottomRightPoint()).isEqualTo(ShapeFactory.INST.createPoint(120, 260));
		});
	}

	@ParameterizedTest
	@MethodSource({"canDoFixtures"})
	void testGetRefPosition(final Runnable config) {
		commonCanDoFixture();
		config.run();
		assertThat(cmd.getRefPosition()).isSameAs(refPosition);
	}

	@Test
	void testHadEffectsY() {
		commonCanDoFixture();
		cmd = new ScaleShapes(shape, drawing, Position.NORTH);
		cmd.setNewY(65);
		cmd.doIt();
		cmd.setNewY(260);
		cmd.doIt();
		cmd.done();
		assertThat(cmd.hadEffect()).isFalse();
	}

	@Test
	void testHadEffectsX() {
		commonCanDoFixture();
		cmd = new ScaleShapes(shape, drawing, Position.EAST);
		cmd.setNewX(55);
		cmd.doIt();
		cmd.setNewX(20);
		cmd.doIt();
		cmd.done();
		assertThat(cmd.hadEffect()).isFalse();
	}

	@Test
	void testHadEffectsNotDone() {
		commonCanDoFixture();
		cmd = new ScaleShapes(shape, drawing, Position.NE);
		cmd.setNewX(55);
		cmd.setNewY(65);
		cmd.doIt();
		assertThat(cmd.hadEffect()).isFalse();
	}
}
