package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekCardBg
import com.example.ui.theme.SleekDarkTerracotta
import com.example.ui.theme.SleekPrimary
import com.example.ui.theme.SleekRoseContainer
import com.example.ui.theme.SleekTextMain
import com.example.ui.theme.SleekTextMuted

@Composable
fun AuthModal(
    initialTabIsSignUp: Boolean = true,
    onSignUp: (name: String, contact: String, pass: String, confirmPass: String, refCode: String?) -> Unit,
    onLogin: (contact: String, pass: String) -> Unit,
    onDismiss: () -> Unit
) {
    var isSignUpTab by remember { mutableStateOf(initialTabIsSignUp) }

    // Form fields
    var fullName by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var referralCode by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SleekCardBg,
            border = BorderStroke(1.dp, SleekBorder),
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
                .testTag("auth_modal_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                // Modal Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SleekRoseContainer,
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = SleekDarkTerracotta,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isSignUpTab) "Create New Account" else "Welcome Back",
                                color = SleekTextMain,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text(
                                text = "Gmail or BD Mobile Number (০১৭XXXXXXXX)",
                                color = SleekTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SleekTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Selector (Sign Up vs Log In)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SleekRoseContainer,
                    border = BorderStroke(1.dp, SleekBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSignUpTab) SleekPrimary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    isSignUpTab = true
                                    errorMessage = null
                                }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            ) {
                                Text(
                                    text = "SIGN UP (সাইন আপ)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isSignUpTab) Color.White else SleekDarkTerracotta
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (!isSignUpTab) SleekPrimary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    isSignUpTab = false
                                    errorMessage = null
                                }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            ) {
                                Text(
                                    text = "LOG IN (লগইন)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (!isSignUpTab) Color.White else SleekDarkTerracotta
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error Message Banner if any
                errorMessage?.let { err ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEE2E2),
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = err,
                            color = Color(0xFFDC2626),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                if (isSignUpTab) {
                    // Sign Up Fields
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name (আপনার সম্পূর্ণ নাম)") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = SleekDarkTerracotta)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPrimary,
                            unfocusedBorderColor = SleekBorder,
                            focusedLabelColor = SleekPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_name_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = contactInfo,
                        onValueChange = { contactInfo = it },
                        label = { Text("Gmail / BD Phone (017XXXXXXXX)") },
                        leadingIcon = {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = SleekDarkTerracotta)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPrimary,
                            unfocusedBorderColor = SleekBorder,
                            focusedLabelColor = SleekPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_contact_input")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // BD Mobile / Gmail helper sample chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SleekRoseContainer,
                            border = BorderStroke(1.dp, SleekBorder),
                            modifier = Modifier.clickable { contactInfo = "01712345678" }
                        ) {
                            Text(
                                text = "📱 BD Phone (01712345678)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekDarkTerracotta,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SleekRoseContainer,
                            border = BorderStroke(1.dp, SleekBorder),
                            modifier = Modifier.clickable { contactInfo = "user@gmail.com" }
                        ) {
                            Text(
                                text = "✉️ Gmail",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekDarkTerracotta,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Set Password (পাসওয়ার্ড)") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = SleekDarkTerracotta)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPrimary,
                            unfocusedBorderColor = SleekBorder,
                            focusedLabelColor = SleekPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_password_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password (পাসওয়ার্ড নিশ্চিত করুন)") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = SleekDarkTerracotta)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPrimary,
                            unfocusedBorderColor = SleekBorder,
                            focusedLabelColor = SleekPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_confirm_password_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = referralCode,
                        onValueChange = { referralCode = it },
                        label = { Text("Referral Code (রেফারেল কোড - ঐচ্ছিক)") },
                        placeholder = { Text("e.g. WATCH2026 (+50 Bonus Coins)") },
                        leadingIcon = {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = SleekDarkTerracotta)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPrimary,
                            unfocusedBorderColor = SleekBorder,
                            focusedLabelColor = SleekPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("signup_referral_code_input")
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            onSignUp(fullName, contactInfo, password, confirmPassword, referralCode.ifBlank { null })
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SleekPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_signup_button")
                    ) {
                        Text(
                            text = "CREATE ACCOUNT (অ্যাকাউন্ট তৈরি করুন)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    // Log In Fields
                    OutlinedTextField(
                        value = contactInfo,
                        onValueChange = { contactInfo = it },
                        label = { Text("Gmail or BD Phone (017XXXXXXXX)") },
                        leadingIcon = {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = SleekDarkTerracotta)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPrimary,
                            unfocusedBorderColor = SleekBorder,
                            focusedLabelColor = SleekPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_contact_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password (পাসওয়ার্ড)") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = SleekDarkTerracotta)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPrimary,
                            unfocusedBorderColor = SleekBorder,
                            focusedLabelColor = SleekPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_password_input")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            onLogin(contactInfo, password)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SleekPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_login_button")
                    ) {
                        Text(
                            text = "LOG IN NOW (লগইন করুন)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
