package com.simple.original.client.view.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;

public class AnimationBuilder {

    public interface AnimationCompletionHandler {

        void onAnimationsComplete();
    }

    private List<BuilderAnimation> animations = new ArrayList<BuilderAnimation>();
    private AnimationCompletionHandler completionHandler;
    private Element element;

    public abstract class BuilderAnimation extends Animation {

        protected void onComplete() {
            super.onComplete();
            AnimationBuilder.this.animationsComplete();
        }
    }

    /**
     * Fade animtion.
     * 
     * @author chinshaw
     * 
     */
    public class FadeAnimation extends BuilderAnimation {

        private boolean fadeIn = false;

        public FadeAnimation() {
            this(false);
        }

        public FadeAnimation(boolean fadeIn) {
            this.fadeIn = fadeIn;
        }

        @Override
        protected void onUpdate(double progress) {
            if (fadeIn) {
                element.getStyle().setOpacity(progress * 1);
            } else {
                element.getStyle().setOpacity(1 - (progress * 1));
            }
        }
    }

    public class SlideAnimation extends BuilderAnimation {

        private boolean opening = false;

        private int height;

        public SlideAnimation(boolean opening) {
            this.opening = opening;
            element.getStyle().setOverflow(Overflow.HIDDEN);

        }

        @Override
        protected void onStart() {
            int paddingTop = getComputedStyle(element, "padding-top");
            int paddingBottom = getComputedStyle(element, "padding-bottom");

            height = element.getClientHeight() - paddingTop - paddingBottom;

            if (opening) {
                DOM.setStyleAttribute(element, "height", 0 + "px");
            }
            super.onStart();
        }

        /**
         * Overriding so that we can set the height back to auto. Clearing the
         * static height and setting it to auto.
         */
        @Override
        protected void onComplete() {
            super.onComplete();
            DOM.setStyleAttribute(element, "height", "auto");
        }

        @Override
        protected void onUpdate(double progress) {
            int scrollHeight = height;

            int height = (int) (progress * scrollHeight);
            if (!opening) {
                height = scrollHeight - height;
            }

            height = Math.max(height, 1);
            Style style = element.getStyle();
            style.setHeight(height, Unit.PX);
            //DOM.setStyleAttribute(element, "width", "auto");
        }
    }

    protected AnimationBuilder(Element element) {
        this.element = element;
    }

    public static AnimationBuilder create(Element element) {
        return new AnimationBuilder(element);
    }

    public AnimationBuilder addAnimation(BuilderAnimation animation) {
        animations.add(animation);
        return this;
    }

    public AnimationBuilder addCompletionHandler(AnimationCompletionHandler completionHandler) {
        this.completionHandler = completionHandler;
        return this;
    }

    public void run(int seconds) {
        for (Animation animation : animations) {
            animation.run(seconds);
        }
    }

    protected void animationsComplete() {
        if (completionHandler != null) {
            completionHandler.onAnimationsComplete();
            completionHandler = null;
        }
    }

    public AnimationBuilder addFadeOutAnimation() {
        animations.add(new FadeAnimation());
        return this;
    }

    public AnimationBuilder addFadeInAnimation() {
        animations.add(new FadeAnimation(true));
        return this;
    }

    public AnimationBuilder addSlideUpAnimation() {
        animations.add(new SlideAnimation(false));
        return this;
    }

    public AnimationBuilder addSlideDownAnimation() {
        animations.add(new SlideAnimation(true));
        return this;
    }

    public void clearAnimations() {
        animations.clear();
    }

    /**
     * Clear the animation chain.
     */
    public void clearCompletionHandlers() {
        completionHandler = null;
    }

    public void reset() {
        clearAnimations();
        clearCompletionHandlers();
    }

    public static native int getComputedStyle(Element elem, String prop)
    /*-{
        var strValue = "";
		if ($doc.defaultView && $doc.defaultView.getComputedStyle) // W3C
		{
			strValue = $doc.defaultView.getComputedStyle(elem, null)
					.getPropertyValue(prop);
		} else if (elem.currentStyle) // IE
		{
			strValue = elem.currentStyle[prop];
		}
		if (strValue == "") { strValue = 0 } 
		return parseInt(strValue);
    }-*/;

}
