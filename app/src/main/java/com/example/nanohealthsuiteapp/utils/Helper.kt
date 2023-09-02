package com.example.nanohealthsuiteapp.utils

import android.view.View
import java.util.regex.Matcher
import java.util.regex.Pattern


fun isValidEmail(email: String): Boolean {
    val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})", Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email)
    return matcher.matches()
}

fun isValidPassword(password: String): Boolean {
    return (password.length >= 6)
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}