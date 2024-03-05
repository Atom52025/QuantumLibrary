import { Card, CardBody } from '@nextui-org/react';
import { useEffect } from 'react';

export default function InfoPopups({ resultModal, setResultModal }) {
  useEffect(() => {
    console.log(resultModal);
    const timer = setTimeout(() => {
      setResultModal('closed');
    }, 5000);

    return () => clearTimeout(timer);
  }, [resultModal]);

  return (
    <>
      {resultModal === 'success' && (
        <Card className="bg-green-500 text-gray-900 text-3xl absolute bottom-3 right-3 z-50">
          <CardBody>
            <p>Game added correctly.</p>
          </CardBody>
        </Card>
      )}
      {resultModal === 'error' && (
        <Card className="bg-red-600 text-gray-900 text-3xl absolute bottom-3 right-3 z-50">
          <CardBody>
            <p>Error.</p>
          </CardBody>
        </Card>
      )}
      {resultModal === 'successDelete' && (
        <Card className="bg-green-500 text-gray-900 text-3xl absolute bottom-3 right-3 z-50">
          <CardBody>
            <p>Game deleted correctly.</p>
          </CardBody>
        </Card>
      )}
      {resultModal === 'successEdit' && (
        <Card className="bg-green-500 text-gray-900 text-3xl absolute bottom-3 right-3 z-50">
          <CardBody>
            <p>Game edited correctly.</p>
          </CardBody>
        </Card>
      )}
    </>
  );
}
