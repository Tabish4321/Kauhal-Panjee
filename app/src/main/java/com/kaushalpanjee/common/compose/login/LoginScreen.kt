package com.kaushalpanjee.common.compose.login

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.kaushalpanjee.R

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LoginScreen(
    loginId: String,
    password: String,
    isLoading: Boolean,
    errorMessage: String,
    currentLanguage: String,
    version: String,
    onLoginIdChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onAboutUnnati: () -> Unit,
    onForgotPassword: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginColors.Background)
    ) {
        val compact = maxHeight < 800.dp
        val veryCompact = maxHeight < 680.dp

        LoginRuralBackground(
            Modifier.align(Alignment.BottomCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = LoginDimensions.ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                Modifier.height(
                    if (veryCompact) 2.dp
                    else if (compact) 5.dp
                    else 12.dp
                )
            )

            // Language icon at the top-right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        // Connect this callback to your language selector.
                        onLanguageSelected(currentLanguage)
                    },
                    modifier = Modifier.size(42.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.languagen
                        ),
                        contentDescription = "Select Language",
                        modifier = Modifier.size(30.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(
                Modifier.height(
                    if (veryCompact) 2.dp else 6.dp
                )
            )

            LoginHeader()

            Spacer(
                Modifier.height(
                    if (veryCompact) 13.dp
                    else if (compact) 20.dp
                    else 35.dp
                )
            )

            Box(Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = LoginShapes.Card,
                    color = LoginColors.Surface,
                    border = BorderStroke(
                        1.dp,
                        LoginColors.Border.copy(alpha = 0.55f)
                    ),
                    shadowElevation = 5.dp
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 20.dp
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(35.dp))

                        Text(
                            text = stringResource(R.string.login_title),
                            color = LoginColors.DarkGreen,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = stringResource(R.string.login_subtitle),
                            modifier = Modifier.padding(top = 4.dp),
                            color = LoginColors.TextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )

                        LoginInputField(
                            title = stringResource(R.string.login_id),
                            placeholder = stringResource(
                                R.string.login_id_enter
                            ),
                            value = loginId,
                            onValueChange = onLoginIdChanged,
                            modifier = Modifier.padding(
                                top = if (veryCompact) 7.dp
                                else if (compact) 10.dp
                                else 17.dp
                            )
                        )

                        LoginInputField(
                            title = stringResource(
                                R.string.login_password
                            ),
                            placeholder = stringResource(
                                R.string.login_password_enter
                            ),
                            value = password,
                            onValueChange = onPasswordChanged,
                            isPassword = true,
                            keyboardType = KeyboardType.Password,
                            modifier = Modifier.padding(
                                top = if (veryCompact) 5.dp
                                else if (compact) 7.dp
                                else 9.dp
                            )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = if (veryCompact) 2.dp else 5.dp
                                ),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = onForgotPassword,
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = buildAnnotatedString {
                                        append(stringResource(R.string.forgot_password))
                                        addStyle(
                                            style = SpanStyle(
                                                textDecoration = TextDecoration.Underline
                                            ),
                                            start = 0,
                                            end = length
                                        )
                                    },
                                    color = LoginColors.DarkGreen,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        LoginErrorMessage(errorMessage)

                        LoginPrimaryButton(
                            title = stringResource(
                                R.string.login
                            ),
                            isLoading = isLoading,
                            onClick = onLogin,
                            modifier = Modifier.padding(
                                top = if (veryCompact) 4.dp
                                else if (compact) 6.dp
                                else 8.dp
                            )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = if (veryCompact) 4.dp
                                    else if (compact) 7.dp
                                    else 10.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = LoginColors.Divider
                            )

                            Text(
                                text = stringResource(R.string.common_or),
                                modifier = Modifier.padding(
                                    horizontal = 10.dp
                                ),
                                color = LoginColors.TextSecondary,
                                fontSize = 10.sp
                            )

                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = LoginColors.Divider
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = if (veryCompact) 4.dp else 6.dp,
                                    bottom = if (veryCompact) 5.dp
                                    else if (compact) 9.dp
                                    else 17.dp
                                ),
                            horizontalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            LoginOutlinedButton(
                                title = stringResource(
                                    R.string.register
                                ),
                                register = true,
                                onClick = onRegister,
                                modifier = Modifier.weight(1f)
                            )

                            LoginOutlinedButton(
                                title = stringResource(
                                    R.string.about_unnati
                                ),
                                register = false,
                                onClick = onAboutUnnati,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                LoginLockBadge(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-35).dp)
                )
            }

            Spacer(
                Modifier.height(
                    if (veryCompact) 3.dp
                    else if (compact) 8.dp
                    else 18.dp
                )
            )

            LoginFooter(version = version)

            Spacer(
                Modifier.height(
                    if (veryCompact) 1.dp else 5.dp
                )
            )
        }
    }
}