import { CheckboxGroup, Spinner } from '@nextui-org/react';
import React from 'react';

export default function Loading() {
  return (
    <>
      <div className="lg:h-full min-w-[200px] bg-gray-800/30 shadow-inner flex flex-col p-3">
        <CheckboxGroup label="Tags Filters">
          <Spinner className="lg:mt-5" />
        </CheckboxGroup>
      </div>
      <main className="min-h-full w-full shadow-inner flex items-center justify-center">
        <Spinner />
      </main>
    </>
  );
}
