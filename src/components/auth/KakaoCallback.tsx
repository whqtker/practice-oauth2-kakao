import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import axios from "axios";
import { backUrl } from "../../constants";

export const KakaoCallback = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const code = searchParams.get("code");

  useEffect(() => {
    // OAuth 인증 코드가 없으면 홈으로 리다이렉트
    if (!code) {
      navigate("/");
      return;
    }

    const fetchAuthData = async () => {
      try {
        const response = await axios.get(`${backUrl}/api/v1/members/me`, {
          withCredentials: true,
        });

        if (response.data) {
          login();
          navigate("/");
        }
      } catch (error) {
        console.error("인증 실패:", error);
        navigate("/");
      }
    };

    fetchAuthData();
  }, [code, login, navigate]);

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="text-center">
        <h2 className="text-xl font-semibold mb-2">로그인 처리 중...</h2>
        <p className="text-gray-600">잠시만 기다려주세요.</p>
      </div>
    </div>
  );
};

export default KakaoCallback;