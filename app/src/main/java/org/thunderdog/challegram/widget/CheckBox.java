/**
 * File created on 14/02/16 at 22:17
 * Copyright Vyacheslav Krylov, 2014
 */
package org.thunderdog.challegram.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.View;

import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.theme.Theme;
import org.thunderdog.challegram.tool.Paints;
import org.thunderdog.challegram.tool.Screen;

import me.vkryl.android.AnimatorUtils;
import me.vkryl.android.widget.FrameLayoutFix;
import me.vkryl.core.ColorUtils;
import me.vkryl.core.MathUtils;

public class CheckBox extends View {
  //private static final int DISABLED_COLOR = 0xff7D858F;
  //private static final int ENABLED_COLOR = 0xff35B7F3;
  //private static final ColorChanger changer = new ColorChanger(DISABLED_COLOR, ENABLED_COLOR);

  private static final int FLAG_CHECKED = 0x01;
  private static final int FLAG_HIDDEN = 0x02;
  private static final int FLAG_DISABLED = 0x04;
  private static final int FLAG_INDETERMINATE = 0x08;

  private float showFactor;
  private float factor;
  private float indeterminateFactor;

  private int flags;

  private final int radius;
  private final int x1, x1pad, y1, lineSize;
  private final int offset;

  private final RectF rect;
  private final Paint outerPaint;
  private final Paint checkPaint;

  public CheckBox (Context context) {
    super(context);

    x1 = Screen.dp(3f);
    x1pad = Screen.dp(4f);
    y1 = Screen.dp(11f);
    lineSize = Screen.dp(1.5f);

    radius = Screen.dp(2f);
    offset = (int) ((float) radius * .5f);

    outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    outerPaint.setColor(Theme.radioOutlineColor());
    outerPaint.setStyle(Paint.Style.STROKE);
    outerPaint.setStrokeWidth(radius);

    checkPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    checkPaint.setStyle(Paint.Style.STROKE);
    checkPaint.setStrokeWidth(4.5f);

    rect = new RectF();
    rect.left = offset;
    rect.top = offset;

    showFactor = 1f;
  }

  public void setChecked (boolean checked, final boolean animated) {
    if ((checked && (flags & FLAG_CHECKED) != 0) || (!checked && (flags & FLAG_CHECKED) == 0)) {
      return;
    }
    if (checked) {
      flags |= FLAG_CHECKED;
    } else {
      flags &= ~ FLAG_CHECKED;
    }
    if (!animated) {
      this.factor = checked ? 1f : 0f;
      invalidate();
      return;
    }
    ValueAnimator obj;
    final float startFactor = getFactor();
    obj = AnimatorUtils.simpleValueAnimator(); //ObjectAnimator.ofFloat(this, "factor", checked ? 1f : 0f);
    if (checked) {
      final float diffFactor = 1f - startFactor;
      obj.addUpdateListener(animation -> setFactor(startFactor + diffFactor * AnimatorUtils.getFraction(animation)));
    } else {
      obj.addUpdateListener(animation -> setFactor(startFactor - startFactor * AnimatorUtils.getFraction(animation)));
    }
    obj.setDuration(165l);
    obj.setInterpolator(AnimatorUtils.DECELERATE_INTERPOLATOR);
    obj.start();
  }

  public void setIndeterminate (boolean checked, final boolean animated) {
    if ((checked && (flags & FLAG_INDETERMINATE) != 0) || (!checked && (flags & FLAG_INDETERMINATE) == 0)) {
      return;
    }
    if (checked) {
      flags |= FLAG_INDETERMINATE;
    } else {
      flags &= ~ FLAG_INDETERMINATE;
    }
    if (!animated) {
      this.indeterminateFactor = checked ? 1f : 0f;
      invalidate();
      return;
    }
    ValueAnimator obj;
    final float startFactor = getIndeterminateFactor();
    obj = AnimatorUtils.simpleValueAnimator(); //ObjectAnimator.ofFloat(this, "factor", checked ? 1f : 0f);
    if (checked) {
      final float diffFactor = 1f - startFactor;
      obj.addUpdateListener(animation -> setIndeterminateFactor(startFactor + diffFactor * AnimatorUtils.getFraction(animation)));
    } else {
      obj.addUpdateListener(animation -> setIndeterminateFactor(startFactor - startFactor * AnimatorUtils.getFraction(animation)));
    }
    obj.setDuration(165l);
    obj.setInterpolator(AnimatorUtils.DECELERATE_INTERPOLATOR);
    obj.start();
  }

  public void setFactor (float factor) {
    if (this.factor != factor) {
      this.factor = factor;
      invalidate();
    }
  }

  public void setIndeterminateFactor (float factor) {
    if (this.indeterminateFactor != factor) {
      this.indeterminateFactor = factor;
      invalidate();
    }
  }

  public float getFactor () {
    return factor;
  }

  public float getIndeterminateFactor () {
    return indeterminateFactor;
  }

  public boolean isIndeterminate () {
    return (flags & FLAG_INDETERMINATE) != 0;
  }

  public void setHidden (boolean hidden, final boolean animated) {
    if ((hidden && (flags & FLAG_HIDDEN) != 0) || (!hidden & (flags & FLAG_HIDDEN) == 0)) {
      return;
    }
    if (hidden) {
      flags |= FLAG_HIDDEN;
    } else {
      flags &= ~FLAG_HIDDEN;
    }
    if (!animated) {
      this.showFactor = hidden ? 0f : 1f;
      invalidate();
      return;
    }
    ValueAnimator obj;
    final float startFactor = getShowFactor();
    obj = AnimatorUtils.simpleValueAnimator();
    if (hidden) {
      obj.addUpdateListener(animation -> setShowFactor(startFactor - startFactor * AnimatorUtils.getFraction(animation)));
    } else {
      final float diffFactor = 1f - startFactor;
      obj.addUpdateListener(animation -> setShowFactor(startFactor + diffFactor * AnimatorUtils.getFraction(animation)));
    }
    obj.setDuration(165l);
    obj.setInterpolator(AnimatorUtils.DECELERATE_INTERPOLATOR);
    obj.start();
  }

  public void setShowFactor (float factor) {
    if (this.showFactor != factor) {
      this.showFactor = factor;
      invalidate();
    }
  }

  public float getShowFactor () {
    return showFactor;
  }

  public boolean toggle () {
    setChecked((flags & FLAG_CHECKED) == 0, true);
    return (flags & FLAG_CHECKED) != 0;
  }

  public boolean toggleIndeterminate () {
    setIndeterminate((flags & FLAG_INDETERMINATE) == 0, true);
    return (flags & FLAG_INDETERMINATE) != 0;
  }

  public void setDisabled (boolean disabled) {
    if ((disabled && (flags & FLAG_DISABLED) != 0) || (!disabled && (flags & FLAG_DISABLED) == 0)) {
      return;
    }
    if (disabled) {
      flags |= FLAG_DISABLED;
    } else {
      flags &= ~FLAG_DISABLED;
    }
    invalidate();
  }

  // Internal

  @Override
  protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
    rect.right = size - offset * 2;
    rect.bottom = size - offset * 2;
  }

  private static final float FACTOR_DIFF = .65f;
  private static final float SCALE_DIFF = .15f;

  @Override
  protected void onDraw (Canvas c) {
    if (showFactor == 0f) {
      return;
    }
    int alpha = (int) (255f * showFactor);

    float rectFactor = Math.min(this.factor / FACTOR_DIFF, 1f);
    float checkFactor = factor <= FACTOR_DIFF ? 0f : (factor - FACTOR_DIFF) / (1f - FACTOR_DIFF);
    float scaleFactor = 1f - (rectFactor == 1f ? 1f - checkFactor : rectFactor) * SCALE_DIFF;

    float cx = (rect.left + rect.right) * .5f;
    float cy = (rect.top + rect.bottom) * .5f;

    c.save();
    c.scale(scaleFactor, scaleFactor, cx, cy);

    int color = ColorUtils.fromToArgb(Theme.radioOutlineColor(), Theme.radioFillingColor(), (flags & FLAG_DISABLED) != 0 ? 0f : rectFactor);
    outerPaint.setColor(color);
    outerPaint.setAlpha(alpha);
    c.drawRoundRect(rect, radius, radius, outerPaint);

    if (rectFactor != 0f) {
      /*paint.setColor(color);
    paint.setAlpha(alpha);*/

      int w = (int) ((rect.right - rect.left - offset * 2) * .5f * rectFactor);
      int h = (int) ((rect.bottom - rect.top - offset * 2) * .5f * rectFactor);

      int left = (int) (rect.left + offset + w);
      int right = (int) (rect.right - offset - w);

      final int alphaColor = ColorUtils.alphaColor(showFactor, color);

      c.drawRect(rect.left + offset, rect.top + offset, left, rect.bottom - offset, Paints.fillingPaint(alphaColor));
      c.drawRect(right, rect.top + offset, rect.right - offset, rect.bottom - offset, Paints.fillingPaint(alphaColor));
      c.drawRect(left, rect.top + offset, right, rect.top + offset + h, Paints.fillingPaint(alphaColor));
      c.drawRect(left, rect.bottom - offset - h, right, rect.bottom - offset, Paints.fillingPaint(alphaColor));

      if (checkFactor != 0f) {
        int w2 = (int) (Screen.dp(8f) * checkFactor);
        int h2 = (int) (Screen.dp(8f) * checkFactor);
        int h1 = (int) (x1pad * checkFactor);
        int x1pad2 = Screen.dp(1f);

        final int checkColor = ColorUtils.alphaColor(showFactor, Theme.radioCheckColor());
        checkPaint.setColor(checkColor);

        int smallSx = x1 + x1pad;
        int smallSy = (int) cy + x1pad;

        if (indeterminateFactor == 1f) {
          float diff = ((rect.right - (x1 * 2)) / 2f) * checkFactor;
          c.drawLine(
            cx - diff,
            cy,
            cx + diff,
            cy,
            checkPaint
          );
        } else {
          c.drawLine(
            smallSx,
            smallSy - (h1 * indeterminateFactor),
            smallSx - h1,
            smallSy - h1,
            checkPaint
          );

          c.drawLine(
            smallSx - x1pad2,
            smallSy - (h1 * indeterminateFactor),
            smallSx + w2 - x1pad2,
            MathUtils.fromTo(smallSy - h2, smallSy - h1, indeterminateFactor),
            checkPaint
          );
        }
      }
    }

    c.restore();
  }

  public static CheckBox simpleCheckBox (Context context) {
    return simpleCheckBox(context, Lang.rtl());
  }

  public static CheckBox simpleCheckBox (Context context, boolean rtl) {
    FrameLayoutFix.LayoutParams params;

    params = FrameLayoutFix.newParams(Screen.dp(18f), Screen.dp(18f));
    params.gravity = Gravity.CENTER_VERTICAL | (rtl ? Gravity.LEFT : Gravity.RIGHT);
    params.leftMargin = params.rightMargin = Screen.dp(19f);

    CheckBox checkBox;
    checkBox = new CheckBox(context);
    checkBox.setLayoutParams(params);

    return checkBox;
  }
}
