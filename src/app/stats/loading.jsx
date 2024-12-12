import { CheckboxGroup, Spinner } from '@nextui-org/react';
import React from 'react';

export default function Loading() {
  return (
    <>
      <main className="min-h-full w-full shadow-inner flex items-center justify-center">
        <Spinner />
      </main>
    </>
  );
}
