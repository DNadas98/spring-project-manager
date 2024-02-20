import {createRoot} from 'react-dom/client'
import './index.css';
import {AppThemeProvider} from "./style/context/AppThemeProvider.js";
import {AppRouterProvider} from "./router/context/AppRouterProvider.js";
import {AuthProvider} from "./auth/context/AuthProvider.tsx";

const rootElement = document.getElementById("root");
const root = createRoot(rootElement as HTMLElement);

root.render(
  <AppThemeProvider>
    <AuthProvider>
      <AppRouterProvider/>
    </AuthProvider>
  </AppThemeProvider>
);