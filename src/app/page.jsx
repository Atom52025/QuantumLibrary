'use client';

import { singIn } from 'next-auth/react';

import ButtonAuth from '@/app/components/ButtonAuth';

export default async function Page() {
  return (
    <main className="bg-gray-900 min-h-full relative ">
      <ButtonAuth />
    </main>
  );
}
