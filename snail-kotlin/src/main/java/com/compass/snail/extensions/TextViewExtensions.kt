//  Copyright © 2017 Compass. All rights reserved.

package com.compass.snail.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.compass.snail.Observable
import com.compass.snail.Replay

inline val TextView.textChanged: Observable<String>
    get() {
        val observable = Replay<String>(1)
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                observable.next(charSequence.toString())
            }
            override fun afterTextChanged(editable: Editable) { }
        })
        return observable
    }
