'use client';

import { NextUIProvider } from '@nextui-org/react';

export function Providers({ children }) {
  return <NextUIProvider className="h-full w-full">{children}</NextUIProvider>;
}
