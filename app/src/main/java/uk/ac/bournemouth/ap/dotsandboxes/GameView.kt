package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.util.AttributeSet
import android.view.View

class GameView: View {
  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  )
}