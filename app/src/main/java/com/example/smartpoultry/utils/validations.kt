package com.example.smartpoultry.utils

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return email.matches(emailRegex)
}

fun checkPasswordLength(password: String) : Boolean{
    return password.length >=6
}
fun isPasswordSame(password: String, confirmPassword : String) : Boolean = password == confirmPassword

fun isValidPhone(phoneNumber : String) : Boolean{
    return phoneNumber.length == 9
}