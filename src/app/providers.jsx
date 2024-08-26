'use client';

import { NextUIProvider } from '@nextui-org/react';
import { SessionProvider } from 'next-auth/react';

export function Providers({ children }) {
  return (
    <SessionProvider>
      <NextUIProvider className="h-full w-full">{children}</NextUIProvider>
    </SessionProvider>
  );
}
