import { Outlet } from 'react-router-dom';

export function MainLayout() {
  return (
    <div className="w-full h-screen flex items-center justify-center bg-gray-50">
      <main className="w-full max-w-full flex items-center justify-center">
        <Outlet />
      </main>
    </div>
  );
}