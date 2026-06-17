package com.bekalmu.pos.ui.home

import androidx.`annotation`.CheckResult
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.bekalmu.pos.R

public class HomeFragmentDirections private constructor() {
  public companion object {
    @CheckResult
    public fun actionHomeToKasir(): NavDirections = ActionOnlyNavDirections(R.id.action_home_to_kasir)

    @CheckResult
    public fun actionHomeToLaporan(): NavDirections = ActionOnlyNavDirections(R.id.action_home_to_laporan)
  }
}
