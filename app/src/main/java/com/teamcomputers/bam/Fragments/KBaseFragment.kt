package com.teamcomputers.bam.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import com.teamcomputers.bam.Activities.BaseActivity
import com.teamcomputers.bam.Interface.KBAMConstant

abstract class KBaseFragment:Fragment(),KBAMConstant {
    val MOVE:Int = 1
    val CUBE = 2
    val FLIP = 3
    val PUSHPULL = 4
    val SIDES = 5
    val CUBEMOVE = 6
    val MOVECUBE = 7
    val PUSHMOVE = 8
    val MOVEPULL = 9
    val FLIPMOVE = 10
    val MOVEFLIP = 11
    val FLIPCUBE = 12
    val CUBEFLIP = 13
    val UP = 1
    val DOWN:Int = 2
    val LEFT = 3
    val RIGHT = 4
    private val DURATION: Long = 1000
    //protected abstract var context: Context

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        /*        switch (BAMUtil.generateRandomInteger(1, 13)) {
            case MOVE:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return MoveAnimation.create(MoveAnimation.UP, enter, DURATION);
                    case DOWN:
                        return MoveAnimation.create(MoveAnimation.DOWN, enter, DURATION);
                    case LEFT:
                        return MoveAnimation.create(MoveAnimation.LEFT, enter, DURATION);
                    case RIGHT:
                        return MoveAnimation.create(MoveAnimation.RIGHT, enter, DURATION);
                }
                break;
            case CUBE:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return CubeAnimation.create(CubeAnimation.UP, enter, DURATION);
                    case DOWN:
                        return CubeAnimation.create(CubeAnimation.DOWN, enter, DURATION);
                    case LEFT:
                        return CubeAnimation.create(CubeAnimation.LEFT, enter, DURATION);
                    case RIGHT:
                        return CubeAnimation.create(CubeAnimation.RIGHT, enter, DURATION);
                }
                break;
            case FLIP:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return FlipAnimation.create(FlipAnimation.UP, enter, DURATION);
                    case DOWN:
                        return FlipAnimation.create(FlipAnimation.DOWN, enter, DURATION);
                    case LEFT:
                        return FlipAnimation.create(FlipAnimation.LEFT, enter, DURATION);
                    case RIGHT:
                        return FlipAnimation.create(FlipAnimation.RIGHT, enter, DURATION);
                }
                break;
            case PUSHPULL:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return PushPullAnimation.create(PushPullAnimation.UP, enter, DURATION);
                    case DOWN:
                        return PushPullAnimation.create(PushPullAnimation.DOWN, enter, DURATION);
                    case LEFT:
                        return PushPullAnimation.create(PushPullAnimation.LEFT, enter, DURATION);
                    case RIGHT:
                        return PushPullAnimation.create(PushPullAnimation.RIGHT, enter, DURATION);
                }
                break;
            case SIDES:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return SidesAnimation.create(SidesAnimation.UP, enter, DURATION);
                    case DOWN:
                        return SidesAnimation.create(SidesAnimation.DOWN, enter, DURATION);
                    case LEFT:
                        return SidesAnimation.create(SidesAnimation.LEFT, enter, DURATION);
                    case RIGHT:
                        return SidesAnimation.create(SidesAnimation.RIGHT, enter, DURATION);
                }
                break;
            case CUBEMOVE:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return enter ? MoveAnimation.create(MoveAnimation.UP, true, DURATION).fading(0.3f, 1.0f) :
                                CubeAnimation.create(CubeAnimation.UP, false, DURATION).fading(1.0f, 0.3f);
                    case DOWN:
                        return enter ? MoveAnimation.create(MoveAnimation.DOWN, true, DURATION).fading(0.3f, 1.0f) :
                                CubeAnimation.create(CubeAnimation.DOWN, false, DURATION).fading(1.0f, 0.3f);
                    case LEFT:
                        return enter ? MoveAnimation.create(MoveAnimation.LEFT, true, DURATION).fading(0.3f, 1.0f) :
                                CubeAnimation.create(CubeAnimation.LEFT, false, DURATION).fading(1.0f, 0.3f);
                    case RIGHT:
                        return enter ? MoveAnimation.create(MoveAnimation.RIGHT, true, DURATION).fading(0.3f, 1.0f) :
                                CubeAnimation.create(CubeAnimation.RIGHT, false, DURATION).fading(1.0f, 0.3f);
                }
                break;
            case MOVECUBE:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return enter ? CubeAnimation.create(CubeAnimation.UP, true, DURATION).fading(0.3f, 1.0f) :
                                MoveAnimation.create(MoveAnimation.UP, false, DURATION).fading(1.0f, 0.3f);
                    case DOWN:
                        return enter ? CubeAnimation.create(CubeAnimation.DOWN, true, DURATION).fading(0.3f, 1.0f) :
                                MoveAnimation.create(MoveAnimation.DOWN, false, DURATION).fading(1.0f, 0.3f);
                    case LEFT:
                        return enter ? CubeAnimation.create(CubeAnimation.LEFT, true, DURATION).fading(0.3f, 1.0f) :
                                MoveAnimation.create(MoveAnimation.LEFT, false, DURATION).fading(1.0f, 0.3f);
                    case RIGHT:
                        return enter ? CubeAnimation.create(CubeAnimation.RIGHT, true, DURATION).fading(0.3f, 1.0f) :
                                MoveAnimation.create(MoveAnimation.RIGHT, false, DURATION).fading(1.0f, 0.3f);
                }
                break;
            case PUSHMOVE:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return enter ? MoveAnimation.create(MoveAnimation.UP, true, DURATION) :
                                PushPullAnimation.create(PushPullAnimation.UP, false, DURATION);
                    case DOWN:
                        return enter ? MoveAnimation.create(MoveAnimation.DOWN, true, DURATION) :
                                PushPullAnimation.create(PushPullAnimation.DOWN, false, DURATION);
                    case LEFT:
                        return enter ? MoveAnimation.create(MoveAnimation.LEFT, true, DURATION) :
                                PushPullAnimation.create(PushPullAnimation.LEFT, false, DURATION);
                    case RIGHT:
                        return enter ? MoveAnimation.create(MoveAnimation.RIGHT, true, DURATION) :
                                PushPullAnimation.create(PushPullAnimation.RIGHT, false, DURATION);
                }
                break;
            case MOVEPULL:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return enter ? PushPullAnimation.create(PushPullAnimation.UP, true, DURATION) :
                                MoveAnimation.create(MoveAnimation.UP, false, DURATION).fading(1.0f, 0.3f);
                    case DOWN:
                        return enter ? PushPullAnimation.create(PushPullAnimation.DOWN, true, DURATION) :
                                MoveAnimation.create(MoveAnimation.DOWN, false, DURATION).fading(1.0f, 0.3f);
                    case LEFT:
                        return enter ? PushPullAnimation.create(PushPullAnimation.LEFT, true, DURATION) :
                                MoveAnimation.create(MoveAnimation.LEFT, false, DURATION).fading(1.0f, 0.3f);
                    case RIGHT:
                        return enter ? PushPullAnimation.create(PushPullAnimation.RIGHT, true, DURATION) :
                                MoveAnimation.create(MoveAnimation.RIGHT, false, DURATION).fading(1.0f, 0.3f);
                }
                break;
            case FLIPMOVE:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return enter ? MoveAnimation.create(MoveAnimation.UP, true, DURATION) :
                                FlipAnimation.create(FlipAnimation.UP, false, DURATION);
                    case DOWN:
                        return enter ? MoveAnimation.create(MoveAnimation.DOWN, true, DURATION) :
                                FlipAnimation.create(FlipAnimation.DOWN, false, DURATION);
                    case LEFT:
                        return enter ? MoveAnimation.create(MoveAnimation.LEFT, true, DURATION) :
                                FlipAnimation.create(FlipAnimation.LEFT, false, DURATION);
                    case RIGHT:
                        return enter ? MoveAnimation.create(MoveAnimation.RIGHT, true, DURATION) :
                                FlipAnimation.create(FlipAnimation.RIGHT, false, DURATION);
                }
                break;
            case MOVEFLIP:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return enter ? FlipAnimation.create(FlipAnimation.UP, true, DURATION) :
                                MoveAnimation.create(MoveAnimation.UP, false, DURATION).fading(1.0f, 0.3f);
                    case DOWN:
                        return enter ? FlipAnimation.create(FlipAnimation.DOWN, true, DURATION) :
                                MoveAnimation.create(MoveAnimation.DOWN, false, DURATION).fading(1.0f, 0.3f);
                    case LEFT:
                        return enter ? FlipAnimation.create(FlipAnimation.LEFT, true, DURATION) :
                                MoveAnimation.create(MoveAnimation.LEFT, false, DURATION).fading(1.0f, 0.3f);
                    case RIGHT:
                        return enter ? FlipAnimation.create(FlipAnimation.RIGHT, true, DURATION) :
                                MoveAnimation.create(MoveAnimation.RIGHT, false, DURATION).fading(1.0f, 0.3f);
                }
                break;
            case FLIPCUBE:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return enter ? CubeAnimation.create(CubeAnimation.UP, true, DURATION) :
                                FlipAnimation.create(FlipAnimation.UP, false, DURATION);
                    case DOWN:
                        return enter ? CubeAnimation.create(CubeAnimation.DOWN, true, DURATION) :
                                FlipAnimation.create(FlipAnimation.DOWN, false, DURATION);
                    case LEFT:
                        return enter ? CubeAnimation.create(CubeAnimation.LEFT, true, DURATION) :
                                FlipAnimation.create(FlipAnimation.LEFT, false, DURATION);
                    case RIGHT:
                        return enter ? CubeAnimation.create(CubeAnimation.RIGHT, true, DURATION) :
                                FlipAnimation.create(FlipAnimation.RIGHT, false, DURATION);
                }
                break;
            case CUBEFLIP:
                switch (BAMUtil.generateRandomInteger(1, 4)) {
                    case UP:
                        return enter ? FlipAnimation.create(FlipAnimation.UP, true, DURATION) :
                                CubeAnimation.create(CubeAnimation.UP, false, DURATION).fading(1.0f, 0.3f);
                    case DOWN:
                        return enter ? FlipAnimation.create(FlipAnimation.DOWN, true, DURATION) :
                                CubeAnimation.create(CubeAnimation.DOWN, false, DURATION).fading(1.0f, 0.3f);
                    case LEFT:
                        return enter ? FlipAnimation.create(FlipAnimation.LEFT, true, DURATION) :
                                CubeAnimation.create(CubeAnimation.LEFT, false, DURATION).fading(1.0f, 0.3f);
                    case RIGHT:
                        return enter ? FlipAnimation.create(FlipAnimation.RIGHT, true, DURATION) :
                                CubeAnimation.create(CubeAnimation.RIGHT, false, DURATION).fading(1.0f, 0.3f);
                }
                break;
        }
        return null;*/
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    abstract fun getFragmentName(): String

    fun showToast(toastMessage: String) {
        (context as BaseActivity).showToast(toastMessage)
    }

    fun showProgress(progressMessage: String) {
        (context as BaseActivity).showProgress(progressMessage)
    }

    fun dismissProgress() {
        (context as BaseActivity).dismissProgress()
    }

    /*@IntDef(MOVE, CUBE, FLIP, PUSHPULL, SIDES, CUBEMOVE, MOVECUBE, PUSHMOVE, MOVEPULL, FLIPMOVE, MOVEFLIP, FLIPCUBE, CUBEFLIP)
    annotation class AnimationStyle*/

    fun toogleProfile(toogle: Boolean?) {
        //toogleProfile(toogle);
    }
}