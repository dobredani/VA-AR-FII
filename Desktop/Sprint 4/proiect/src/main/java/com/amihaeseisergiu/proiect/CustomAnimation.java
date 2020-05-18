/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.control.Label;

/**
 *
 * @author Sergiu
 */
public class CustomAnimation {
    
    public static void animateInFromRightWithBounce(double from, Node slide)
    {
        slide.translateXProperty().set(from);
        Timeline moveIn = new Timeline();
        KeyValue kv = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.7), kv);
        moveIn.getKeyFrames().add(kf);

        Timeline moveOut = new Timeline();
        KeyValue kv2 = new KeyValue(slide.translateXProperty(), 300, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), kv2);
        moveOut.getKeyFrames().add(kf2);

        Timeline moveIn2 = new Timeline();
        KeyValue kv3 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf3 = new KeyFrame(Duration.seconds(0.2), kv3);
        moveIn2.getKeyFrames().add(kf3);

        Timeline moveOut2 = new Timeline();
        KeyValue kv4 = new KeyValue(slide.translateXProperty(), 100, Interpolator.EASE_OUT);
        KeyFrame kf4 = new KeyFrame(Duration.seconds(0.2), kv4);
        moveOut2.getKeyFrames().add(kf4);

        Timeline moveIn3 = new Timeline();
        KeyValue kv5 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf5 = new KeyFrame(Duration.seconds(0.2), kv5);
        moveIn3.getKeyFrames().add(kf5);

        Timeline moveOut3 = new Timeline();
        KeyValue kv6 = new KeyValue(slide.translateXProperty(), 20, Interpolator.EASE_OUT);
        KeyFrame kf6 = new KeyFrame(Duration.seconds(0.1), kv6);
        moveOut3.getKeyFrames().add(kf6);

        Timeline moveIn4 = new Timeline();
        KeyValue kv7 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf7 = new KeyFrame(Duration.seconds(0.1), kv7);
        moveIn4.getKeyFrames().add(kf7);

        SequentialTransition sequence = new SequentialTransition(moveIn, moveOut, moveIn2, moveOut2, moveIn3, moveOut3, moveIn4);
        sequence.play();
    }
    
    public static void animateInFromRightWithBounceSmall(double from, Node slide)
    {
        slide.translateXProperty().set(from);
        Timeline moveIn = new Timeline();
        KeyValue kv = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.2), kv);
        moveIn.getKeyFrames().add(kf);

        Timeline moveOut = new Timeline();
        KeyValue kv2 = new KeyValue(slide.translateXProperty(), 60, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), kv2);
        moveOut.getKeyFrames().add(kf2);

        Timeline moveIn2 = new Timeline();
        KeyValue kv3 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf3 = new KeyFrame(Duration.seconds(0.2), kv3);
        moveIn2.getKeyFrames().add(kf3);

        Timeline moveOut2 = new Timeline();
        KeyValue kv4 = new KeyValue(slide.translateXProperty(), 20, Interpolator.EASE_OUT);
        KeyFrame kf4 = new KeyFrame(Duration.seconds(0.2), kv4);
        moveOut2.getKeyFrames().add(kf4);

        Timeline moveIn3 = new Timeline();
        KeyValue kv5 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf5 = new KeyFrame(Duration.seconds(0.2), kv5);
        moveIn3.getKeyFrames().add(kf5);

        Timeline moveOut3 = new Timeline();
        KeyValue kv6 = new KeyValue(slide.translateXProperty(), 5, Interpolator.EASE_OUT);
        KeyFrame kf6 = new KeyFrame(Duration.seconds(0.1), kv6);
        moveOut3.getKeyFrames().add(kf6);

        Timeline moveIn4 = new Timeline();
        KeyValue kv7 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf7 = new KeyFrame(Duration.seconds(0.1), kv7);
        moveIn4.getKeyFrames().add(kf7);

        SequentialTransition sequence = new SequentialTransition(moveIn, moveOut, moveIn2, moveOut2, moveIn3, moveOut3, moveIn4);
        sequence.play();
    }
    
    public static void animateInFromLeftWithBounceSmall(double from, Node slide)
    {
        slide.translateXProperty().set(-from);
        Timeline moveIn = new Timeline();
        KeyValue kv = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.2), kv);
        moveIn.getKeyFrames().add(kf);

        Timeline moveOut = new Timeline();
        KeyValue kv2 = new KeyValue(slide.translateXProperty(), -60, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), kv2);
        moveOut.getKeyFrames().add(kf2);

        Timeline moveIn2 = new Timeline();
        KeyValue kv3 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf3 = new KeyFrame(Duration.seconds(0.2), kv3);
        moveIn2.getKeyFrames().add(kf3);

        Timeline moveOut2 = new Timeline();
        KeyValue kv4 = new KeyValue(slide.translateXProperty(), -20, Interpolator.EASE_OUT);
        KeyFrame kf4 = new KeyFrame(Duration.seconds(0.2), kv4);
        moveOut2.getKeyFrames().add(kf4);

        Timeline moveIn3 = new Timeline();
        KeyValue kv5 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf5 = new KeyFrame(Duration.seconds(0.2), kv5);
        moveIn3.getKeyFrames().add(kf5);

        Timeline moveOut3 = new Timeline();
        KeyValue kv6 = new KeyValue(slide.translateXProperty(), -5, Interpolator.EASE_OUT);
        KeyFrame kf6 = new KeyFrame(Duration.seconds(0.1), kv6);
        moveOut3.getKeyFrames().add(kf6);

        Timeline moveIn4 = new Timeline();
        KeyValue kv7 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf7 = new KeyFrame(Duration.seconds(0.1), kv7);
        moveIn4.getKeyFrames().add(kf7);

        SequentialTransition sequence = new SequentialTransition(moveIn, moveOut, moveIn2, moveOut2, moveIn3, moveOut3, moveIn4);
        sequence.play();
    }
    
    public static void animateInFromBottomWithBounceSmall(double from, Node slide)
    {
        slide.translateYProperty().set(from);
        Timeline moveIn = new Timeline();
        KeyValue kv = new KeyValue(slide.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.2), kv);
        moveIn.getKeyFrames().add(kf);

        Timeline moveOut = new Timeline();
        KeyValue kv2 = new KeyValue(slide.translateYProperty(), 60, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), kv2);
        moveOut.getKeyFrames().add(kf2);

        Timeline moveIn2 = new Timeline();
        KeyValue kv3 = new KeyValue(slide.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf3 = new KeyFrame(Duration.seconds(0.2), kv3);
        moveIn2.getKeyFrames().add(kf3);

        Timeline moveOut2 = new Timeline();
        KeyValue kv4 = new KeyValue(slide.translateYProperty(), 20, Interpolator.EASE_OUT);
        KeyFrame kf4 = new KeyFrame(Duration.seconds(0.2), kv4);
        moveOut2.getKeyFrames().add(kf4);

        Timeline moveIn3 = new Timeline();
        KeyValue kv5 = new KeyValue(slide.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf5 = new KeyFrame(Duration.seconds(0.2), kv5);
        moveIn3.getKeyFrames().add(kf5);

        Timeline moveOut3 = new Timeline();
        KeyValue kv6 = new KeyValue(slide.translateYProperty(), 5, Interpolator.EASE_OUT);
        KeyFrame kf6 = new KeyFrame(Duration.seconds(0.1), kv6);
        moveOut3.getKeyFrames().add(kf6);

        Timeline moveIn4 = new Timeline();
        KeyValue kv7 = new KeyValue(slide.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf7 = new KeyFrame(Duration.seconds(0.1), kv7);
        moveIn4.getKeyFrames().add(kf7);

        SequentialTransition sequence = new SequentialTransition(moveIn, moveOut, moveIn2, moveOut2, moveIn3, moveOut3, moveIn4);
        sequence.play();
    }
    
    public static void animateInFromTopWithBounceSmall(double from, Node slide)
    {
        slide.translateYProperty().set(-from);
        Timeline moveIn = new Timeline();
        KeyValue kv = new KeyValue(slide.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.2), kv);
        moveIn.getKeyFrames().add(kf);

        Timeline moveOut = new Timeline();
        KeyValue kv2 = new KeyValue(slide.translateYProperty(), -60, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), kv2);
        moveOut.getKeyFrames().add(kf2);

        Timeline moveIn2 = new Timeline();
        KeyValue kv3 = new KeyValue(slide.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf3 = new KeyFrame(Duration.seconds(0.2), kv3);
        moveIn2.getKeyFrames().add(kf3);

        Timeline moveOut2 = new Timeline();
        KeyValue kv4 = new KeyValue(slide.translateYProperty(), -20, Interpolator.EASE_OUT);
        KeyFrame kf4 = new KeyFrame(Duration.seconds(0.2), kv4);
        moveOut2.getKeyFrames().add(kf4);

        Timeline moveIn3 = new Timeline();
        KeyValue kv5 = new KeyValue(slide.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf5 = new KeyFrame(Duration.seconds(0.2), kv5);
        moveIn3.getKeyFrames().add(kf5);

        Timeline moveOut3 = new Timeline();
        KeyValue kv6 = new KeyValue(slide.translateYProperty(), -5, Interpolator.EASE_OUT);
        KeyFrame kf6 = new KeyFrame(Duration.seconds(0.1), kv6);
        moveOut3.getKeyFrames().add(kf6);

        Timeline moveIn4 = new Timeline();
        KeyValue kv7 = new KeyValue(slide.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf7 = new KeyFrame(Duration.seconds(0.1), kv7);
        moveIn4.getKeyFrames().add(kf7);

        SequentialTransition sequence = new SequentialTransition(moveIn, moveOut, moveIn2, moveOut2, moveIn3, moveOut3, moveIn4);
        sequence.play();
    }
    
    public static void animateOutToLeftAndRemove(double from, Node slide, List<Node> removeFrom)
    {
        Timeline moveOut = new Timeline();
        KeyValue kv = new KeyValue(slide.translateXProperty(), -from, Interpolator.EASE_OUT);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.35), kv);
        moveOut.getKeyFrames().add(kf);
        
        SequentialTransition sequence = new SequentialTransition(moveOut);
        sequence.play();
        
        sequence.setOnFinished(e -> {
            removeFrom.remove(slide);
        });
    }
    
    public static void animateInFromLeftWithBounce(double from, Node slide)
    {
        slide.translateXProperty().set(-from);
        Timeline moveIn = new Timeline();
        KeyValue kv = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.7), kv);
        moveIn.getKeyFrames().add(kf);

        Timeline moveOut = new Timeline();
        KeyValue kv2 = new KeyValue(slide.translateXProperty(), -300, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), kv2);
        moveOut.getKeyFrames().add(kf2);

        Timeline moveIn2 = new Timeline();
        KeyValue kv3 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf3 = new KeyFrame(Duration.seconds(0.2), kv3);
        moveIn2.getKeyFrames().add(kf3);

        Timeline moveOut2 = new Timeline();
        KeyValue kv4 = new KeyValue(slide.translateXProperty(), -100, Interpolator.EASE_OUT);
        KeyFrame kf4 = new KeyFrame(Duration.seconds(0.2), kv4);
        moveOut2.getKeyFrames().add(kf4);

        Timeline moveIn3 = new Timeline();
        KeyValue kv5 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf5 = new KeyFrame(Duration.seconds(0.2), kv5);
        moveIn3.getKeyFrames().add(kf5);

        Timeline moveOut3 = new Timeline();
        KeyValue kv6 = new KeyValue(slide.translateXProperty(), -20, Interpolator.EASE_OUT);
        KeyFrame kf6 = new KeyFrame(Duration.seconds(0.1), kv6);
        moveOut3.getKeyFrames().add(kf6);

        Timeline moveIn4 = new Timeline();
        KeyValue kv7 = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf7 = new KeyFrame(Duration.seconds(0.1), kv7);
        moveIn4.getKeyFrames().add(kf7);

        SequentialTransition sequence = new SequentialTransition(moveIn, moveOut, moveIn2, moveOut2, moveIn3, moveOut3, moveIn4);
        sequence.play();
        
    }
    
    public static void animateInFromLeft(double from, Node slide)
    {
        slide.translateXProperty().set(-from);
        Timeline moveOut = new Timeline();
        KeyValue kv = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.8), kv);
        moveOut.getKeyFrames().add(kf);
        
        SequentialTransition sequence = new SequentialTransition(moveOut);
        sequence.play();
        
    }
    
    public static void animateInFromTop(double from, Node slide)
    {
        slide.translateYProperty().set(-from);
        Timeline moveOut = new Timeline();
        KeyValue kv = new KeyValue(slide.translateYProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.8), kv);
        moveOut.getKeyFrames().add(kf);
        
        SequentialTransition sequence = new SequentialTransition(moveOut);
        sequence.play();
        
    }
    
    public static void animateInFromRight(double from, Node slide)
    {
        slide.translateXProperty().set(from);
        Timeline moveOut = new Timeline();
        KeyValue kv = new KeyValue(slide.translateXProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.8), kv);
        moveOut.getKeyFrames().add(kf);
        
        SequentialTransition sequence = new SequentialTransition(moveOut);
        sequence.play();
        
    }
    
    public static void animateInFromBottom(double from, Node slide)
    {
        slide.translateYProperty().set(from);
        Timeline moveOut = new Timeline();
        KeyValue kv = new KeyValue(slide.translateYProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.8), kv);
        moveOut.getKeyFrames().add(kf);
        
        SequentialTransition sequence = new SequentialTransition(moveOut);
        sequence.play();
        
    }
    
    public static void animateTypeWriterText(Label lbl, String descImp) {
        String content = descImp;
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(2500));
            }

            @Override
            protected void interpolate(double frac) {
                final int length = content.length();
                final int n = Math.round(length * (float) frac);
                lbl.setText(content.substring(0, n));
            }
        };
        animation.play();

    }
}
