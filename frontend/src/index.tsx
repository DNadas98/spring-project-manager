import {createRoot} from "react-dom/client";
import {AppThemeProvider} from "./theme/context/AppThemeProvider.tsx";
import {AuthProvider} from "./auth/context/AuthProvider.tsx";
import {DevSupport} from "@react-buddy/ide-toolbox";
import {ComponentPreviews, useInitial} from "./react_buddy_dev";
import {ThemePaletteModeProvider} from "./theme/context/ThemePaletteModeProvider.tsx";
import appRouter from "./config/routing/appRouter.tsx";
import {RouterProvider} from "react-router-dom";

const rootElement = document.getElementById("root");
const root = createRoot(rootElement as HTMLElement);
const router = appRouter;

root.render(
  <DevSupport ComponentPreviews={ComponentPreviews} useInitialHook={useInitial}>
    <ThemePaletteModeProvider>
      <AppThemeProvider>
        <AuthProvider>
          <RouterProvider router={router}/>
        </AuthProvider>
      </AppThemeProvider>
    </ThemePaletteModeProvider>
  </DevSupport>
);
