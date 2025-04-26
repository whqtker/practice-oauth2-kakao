import { RouteObject } from "react-router-dom";
import { Home } from "./components/Home";
import KakaoCallback from "./components/auth/KakaoCallback";
import { MainLayout } from "./layouts/MainLayout";
import { AuthLayout } from "./layouts/AuthLayout";

export const routes: RouteObject[] = [
  {
    path: "/",
    element: <MainLayout />,
    children: [
      {
        path: "",
        element: <Home />,
      },
    ],
  },
  {
    path: "/auth",
    element: <AuthLayout />,
    children: [
      {
        path: "/auth/kakao/callback",
        element: <KakaoCallback />,
      },
    ],
  },
];