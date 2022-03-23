package com.pi.androidbasehiltmvvm.core.extensions

import java.util.concurrent.TimeUnit

/**
 * Format 00:00 (minute:second) [String]
 */
fun Long.formatTime(): String {
    return String.format(
        "%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(this),
        TimeUnit.MILLISECONDS.toMinutes(this) -
            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this)), // The change is in this line
        TimeUnit.MILLISECONDS.toSeconds(this) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
    )
}
