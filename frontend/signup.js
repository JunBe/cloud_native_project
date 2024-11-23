document.addEventListener("DOMContentLoaded", () => {
    const signupForm = document.getElementById("signup-form");

    signupForm.addEventListener("submit", async (event) => {
        event.preventDefault(); // 기본 폼 제출 동작 방지

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("password-confirm").value;

        // 비밀번호 확인
        if (password !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        try {
            // 서버로 회원가입 요청
            const response = await fetch("http://192.168.152.128:8080/api/auth/signup", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    username: username,
                    password: password,
                }),
                credentials: "include"
            });

            if (response.ok) {
                alert("회원가입 성공! 로그인 페이지로 이동합니다.");
                window.location.href = "login.html"; // 로그인 페이지로 이동
            } else {
                const error = await response.json();
                alert(`회원가입 실패: ${error.message}`);
            }
        } catch (error) {
            console.error("회원가입 요청 중 오류:", error);
            alert("회원가입 중 문제가 발생했습니다. 다시 시도해주세요.");
        }
    });
});
