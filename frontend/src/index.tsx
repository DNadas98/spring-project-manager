import {createRoot} from "react-dom/client";
import {AppThemeProvider} from "./common/theme/context/AppThemeProvider.tsx";
import {AuthenticationProvider} from "./authentication/context/AuthenticationProvider.tsx";
import {DevSupport} from "@react-buddy/ide-toolbox";
import {ComponentPreviews, useInitial} from "./react_buddy_dev";
import {ThemePaletteModeProvider} from "./common/theme/context/ThemePaletteModeProvider.tsx";
import appRouter from "./common/routing/appRouter.tsx";
import {RouterProvider} from "react-router-dom";
import {NotificationProvider} from "./common/notification/context/NotificationProvider.tsx";

const rootElement = document.getElementById("root");
const root = createRoot(rootElement as HTMLElement);
const router = appRouter;

root.render(
  <DevSupport ComponentPreviews={ComponentPreviews} useInitialHook={useInitial}>
    <ThemePaletteModeProvider>
      <AppThemeProvider>
        <NotificationProvider>
          <AuthenticationProvider>
            <RouterProvider router={router}/>
          </AuthenticationProvider>
        </NotificationProvider>
      </AppThemeProvider>
    </ThemePaletteModeProvider>
  </DevSupport>
);