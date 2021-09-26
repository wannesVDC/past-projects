package jumpingalien.internal.gui.painters;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLayout;

import jumpingalien.internal.gui.AlienGameScreen;
import jumpingalien.internal.gui.painters.AbstractAlienPainter;
import jumpingalien.internal.game.WorldInfoProvider;

public final class GameOverPainter extends
		AbstractAlienPainter {

	private final WorldInfoProvider worldInfoProvider;

	public GameOverPainter(AlienGameScreen screen,
			WorldInfoProvider worldInfoProvider) {
		super(screen);
		this.worldInfoProvider = worldInfoProvider;
	}

	@Override
	public void paintScreenPost(Graphics2D g) {
		worldInfoProvider.isGameOver().ifPresent(gameOver -> {
			if (gameOver) {
				worldInfoProvider.didPlayerWin().ifPresent(winner -> {
					if (winner) {
						paintGameOverWinner(g);
					} else {
						paintGameOverLoser(g);
					}
				});
			}
		});
	}

	private void paintGameOverLoser(Graphics2D g) {
		paintGameOverDefault(g);

		TextLayout textTl = new TextLayout("Game over", new Font(
				Font.SANS_SERIF, Font.BOLD, 120), g.getFontRenderContext());
		Shape outline = textTl.getOutline(null);
		Rectangle outlineBounds = outline.getBounds();
		g.translate((getScreenWidth() - outlineBounds.getWidth()) / 2,
				(getScreenHeight() - outlineBounds.getHeight() / 2) / 2);
		g.setColor(Color.WHITE);
		g.fill(outline);
		g.setColor(Color.GRAY);
		g.setStroke(new BasicStroke(3.0f));
		g.draw(outline);
	}

	private void paintGameOverDefault(Graphics2D g) {
		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, getScreenWidth(), getScreenHeight());

	}

	private void paintGameOverWinner(Graphics2D g) {
		paintGameOverDefault(g);

		TextLayout textTl = new TextLayout("You won!", new Font(
				Font.SANS_SERIF, Font.BOLD, 120), g.getFontRenderContext());
		Shape outline = textTl.getOutline(null);
		Rectangle outlineBounds = outline.getBounds();
		g.translate((getScreenWidth() - outlineBounds.getWidth()) / 2,
				(getScreenHeight() - outlineBounds.getHeight() / 2) / 2);
		g.setColor(Color.WHITE);
		g.fill(outline);
		g.setColor(Color.GRAY);
		g.setStroke(new BasicStroke(3.0f));
		g.draw(outline);
	}
}