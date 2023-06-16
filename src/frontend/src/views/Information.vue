<template>
    <div class="container">
        <el-form :model="form" :rules="rules" ref="loginFormRef" label-width="120px" class="custom-form">
            <h3 style="color: black">개인 정보 수정</h3>
            <el-form-item label="이메일">
                {{ form.email }}
            </el-form-item>
            <el-form-item label="기존비밀번호" prop="oldPassword" @keyup="checkPasswordAvailability" required>
                <el-input v-model="form.oldPassword" type="password" placeholder="비밀번호 입력"></el-input>
            </el-form-item>
            <el-button class="mt-1" @click="passwordButton">비밀번호 변경</el-button>
            <el-form-item v-if="passwordEdit" label="비밀번호" prop="password" required>
                <el-input v-model="form.password" type="password" placeholder="비밀번호 입력"></el-input>
            </el-form-item>
            <el-form-item v-if="passwordEdit" label="비밀번호 확인" prop="confirmPassword" required>
                <el-input v-model="form.confirmPassword" type="password" placeholder="비밀번호 다시입력"></el-input>
            </el-form-item>
            <el-form-item label="닉네임" prop="nickName" required>
                <el-input type="text" v-model="form.nickName" placeholder="닉네임 입력"/>
            </el-form-item>
            <el-button class="m-lg-1" @click="checkNameAvailability" type="primary">중복 검증</el-button>
            <el-form-item label="이름" prop="name" required>
                <el-input v-model="form.name" placeholder="이름 입력"></el-input>
            </el-form-item>
            <!--                <el-form-item label="성별" prop="gender" required>-->
            <!--                    <el-radio-group v-model="form.gender">-->
            <!--                        <el-radio label="남">남</el-radio>-->
            <!--                        <el-radio label="여">여</el-radio>-->
            <!--                    </el-radio-group>-->
            <!--                </el-form-item>                -->
            <!--                <el-form-item label="핸드폰 번호" prop="phoneNumber" required>-->
            <!--                <el-input v-model="form.phoneNumber" type="tel" placeholder="핸드폰 번호 입력"></el-input>-->
            <!--                </el-form-item>-->

            <el-form-item label="생년월일" prop="birthDate" required>
                <el-input v-model="form.birthDate" type="date"/>
            </el-form-item>
            <el-form-item label="주소">
                {{ form.address }} <br>
                <el-button class="mt-0" @click="addressButton">주소변경</el-button>
            </el-form-item>
            <el-form-item v-if="addressEdit" label="주소입력">
                <el-input type="button" @click="sample6_execDaumPostcode" value="주소검색"/>
                <el-form-item prop="add1" required>
                    <el-input type="text" v-model="form.add1" id="sample6_postcode" placeholder="우편번호"/>
                </el-form-item>
                <el-form-item prop="add2" required>
                    <el-input type="text" v-model="form.add2" id="sample6_address" placeholder="주소"/>
                </el-form-item>
                <el-form-item prop="add3" required>
                    <el-input type="text" v-model="form.add3" id="sample6_extraAddress" placeholder="참고사항"/>
                </el-form-item>
                <el-form-item prop="add4" required>
                    <el-input type="text" v-model="form.add4" id="sample6_detailAddress" placeholder="상세주소"/>
                </el-form-item>
            </el-form-item>

            <el-button type="primary" @click="submitForm">수정</el-button>
            <el-button type="danger" @click="deleteForm">탈퇴</el-button>
        </el-form>
    </div>
</template>

<script setup>
import {computed, onMounted, ref, watch, watchEffect} from 'vue';
import axios from 'axios';
import router from "@/router";
import {showCustomAlert} from "@/main";
import Cookies from "vue-cookies";
import store from "@/stores/store";

const addressEdit = ref(false)
const addressButton = () => {
    addressEdit.value = !addressEdit.value;
}
const passwordEdit = ref(false)
const passwordButton = () => {
    passwordEdit.value = !passwordEdit.value;
}
const form = ref({
    email: '',
    password: '',
    confirmPassword: '',
    name: '',
    nickName: '',
    birthDate: '',
    address: '',
    add1: '',
    add2: '',
    add3: '',
    add4: '',
    oldPassword: ''
})


const rules = {
    oldPassword: [
        {required: true, message: '비밀번호를 입력해주세요', trigger: 'blur'},
        {min: 6, message: '비밀번호는 최소 6자 이상이어야 합니다', trigger: 'blur'},
    ],
    password: [
        {required: true, message: '비밀번호를 입력해주세요', trigger: 'blur'},
        {min: 6, message: '비밀번호는 최소 6자 이상이어야 합니다', trigger: 'blur'},
    ],
    confirmPassword: [
        {required: true, message: '비밀번호 확인을 입력해주세요', trigger: 'blur'},
        {validator: validateConfirmPassword, trigger: 'blur'},
    ],
    name: [
        {required: true, message: '이름을 입력해주세요', trigger: 'blur'},
    ],
    nickName: [
        {required: true, message: '닉네임을 입력해주세요', trigger: 'blur'},
    ],
    birthDate: [
        {required: true, message: '생년월일을 입력해주세요', trigger: 'blur'},
    ],
    add1: [
        {required: true, message: '우편번호를 입력해주세요', trigger: 'blur'},
    ],
    add2: [
        {required: true, message: '주소를 입력해주세요', trigger: 'blur'},
    ],
    add3: [
        {required: true, message: '참고사항을 입력해주세요', trigger: 'blur'},
    ],
    add4: [
        {required: true, message: '상세주소를 입력해주세요', trigger: 'blur'},
    ],

}

function validateConfirmPassword(rule, value, callback) {
    if (value !== form.value.password) {
        callback(new Error('비밀번호가 일치하지 않습니다'));
    } else {
        callback();
    }
}

const checkPasswordAvailability = () => {
    const token = Cookies.get('accessToken');
    axios
        .post('/api/auth/user/password', {
            password: form.value.oldPassword,
        }, {
            headers: {
                Authorization: token
            }
        })
        .then((response) => {
            const valid = response.data.authResult;
            console.log(valid)
            const showCustomAlert = (message) => {
                const customAlert = document.createElement('div');
                customAlert.classList.add('custom-alert');
                customAlert.textContent = message;

                document.body.appendChild(customAlert);

                setTimeout(() => {
                    customAlert.remove();
                }, 2500);
            }

            if (valid) {
                showCustomAlert('기존 비밀번호와 일치합니다.');
            } /*else if (name == form.value.nickname) {
                showCustomAlert('사용 가능한 닉네임입니다.');*/
            else {
                showCustomAlert('기존 비밀번호와 일치하지 않습니다.');
            }
        })
        .catch((error) => {
            if (error.response) {
                const errorMessage = error.response.data.message;
                showCustomAlert(`${errorMessage}`)
            }
        });
};
const checkNameAvailability = () => {
    console.log(form.value.email)
    axios
        .post('/api/auth/signup/nickName', {
            nickName: form.value.nickName,
            email: form.value.email
        })
        .then((response) => {
            const valid = response.data.authResult;
            console.log(valid)
            const name = response.data.name;
            const showCustomAlert = (message) => {
                const customAlert = document.createElement('div');
                customAlert.classList.add('custom-alert');
                customAlert.textContent = message;

                document.body.appendChild(customAlert);

                setTimeout(() => {
                    customAlert.remove();
                }, 2500);
            }

            if (valid) {
                showCustomAlert('사용 가능한 닉네임입니다.');
            } /*else if (name == form.value.nickname) {
                showCustomAlert('사용 가능한 닉네임입니다.');*/
            else {
                showCustomAlert('중복된 닉네임입니다.');
            }
        })
        .catch((error) => {
            if (error.response) {
                const errorMessage = error.response.data.message;
                showCustomAlert(`${errorMessage}`)
            }
        });
};
const loginFormRef = ref();
const submitForm = () => {
    const token = Cookies.get('accessToken');
    loginFormRef.value.validate((valid) => {
        if (valid) {
            const combinedValue = ref()
            if (addressEdit.value == true) {
                combinedValue.value = form.value.add1 + " " + form.value.add2 + form.value.add3 + " " + form.value.add4;
            } else {
                combinedValue.value = form.value.address;
            }
            console.log(form.value.birthDate.toString())
            axios
                .patch('/api/user', {
                    email: form.value.email,
                    password: form.value.oldPassword,
                    newPassword: form.value.password,
                    nickName: form.value.nickName,
                    name: form.value.name,
                    address: combinedValue.value,
                    birthDate: form.value.birthDate.toString(),
                }, {
                    headers: {
                        Authorization: token
                    }
                })
                .then(() => {
                    showCustomAlert("개인정보수정이 완료되었습니다.")
                    router.replace("/");
                })
                .catch((error) => {
                    // if (error.response) {
                    //     const errorMessage = error.response.data.errors[0].defaultMessage;
                    //     showCustomAlert(`${errorMessage}`)
                    // }
                    if (error.response) {
                        const errorMessage = error.response.data.message;
                        showCustomAlert(`${errorMessage}`)
                    }

                });
        }
    })
}
const deleteForm = () => {
    const token = Cookies.get('accessToken');
    loginFormRef.value.validate((valid) => {
        if (valid) {
            axios
                .delete(`/api/user/${form.value.userId}`, {
                    headers: {
                        Authorization: token,
                    },
                })
                .then(() => {
                    showCustomAlert("삭제가 완료되었습니다.")
                    store.commit("setToken", ""); // 로그아웃 시 토큰 초기화
                    Cookies.remove('accessToken'); // 쿠키에서 access token 값 삭제
                    Cookies.remove('refreshToken'); // 쿠키에서 refresh token 값 삭제
                    router.replace("/").then(() => {
                        window.location.reload();
                    })
                })
                .catch((error) => {
                    console.log(error.response.data)
                    if (error.response) {
                        const errorMessage = error.response.data.message;
                        showCustomAlert(`${errorMessage}`)
                    }
                });
        }
    })
}

function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if (data.userSelectedType === 'R') {
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("sample6_extraAddress").value = extraAddr;

            } else {
                document.getElementById("sample6_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("sample6_detailAddress").focus();
            form.value.add1 = data.zonecode;
            form.value.add2 = addr;
            form.value.add3 = extraAddr;
        }
    }).open();
}

onMounted(() => {
    const token = Cookies.get('accessToken');
    axios.post(`/api/auth/user/auth`,
        {},
        {
            headers: {
                Authorization: token
            }
        }).then((response) => {
        form.value.email = response.data.email;
        form.value.nickName = response.data.nickName;
        form.value.name = response.data.name
        form.value.birthDate = response.data.birthDate
        form.value.address = response.data.address
        form.value.userId = response.data.userId;
    }).catch((error) => {
        if (error.response) {
            const errorMessage = error.response.data.message;
            showCustomAlert(`${errorMessage}`)
        }
    })
})
</script>
<style scoped>
/*.con {*/
/*    display: flex;*/
/*    justify-content: center;*/
/*    align-items: center;*/
/*    height: 100vh;*/
/*}*/

.container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    margin-top: 20vh;
    margin-bottom: 20vh;
}

.custom-form {
    background-color: #ffffff;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.custom-form .el-form-item__label {
    color: #333;
    font-weight: bold;
}

.full-width-input {
    width: 100%;
}

.custom-form .el-form-item__error {
    color: #f56c6c;
    font-size: 12px;
    margin-top: 5px;
}

.custom-form .el-form-item__error::before {
    content: '* ';
}

.custom-form .el-input {
}

.custom-form .el-button {
    margin-top: 20px;

}
</style>