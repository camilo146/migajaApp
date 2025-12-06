import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule],
  template: `
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h3>MigajerosBucaramanga</h3>
          <p>Conece las historias mas picantes  de la ciudad bonita, comparte la tuya o la de alguien y unete a la comunidad migajeros .</p>
        </div>
        <div class="footer-section">
          <h4>Enlaces Rápidos</h4>
          <ul>
            <li><a href="/stories">Historias</a></li>
            <li><a href="/tournaments">Torneos</a></li>
            <li><a href="/login">Iniciar Sesión</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>Contacto</h4>
          <p>Bucaramanga, Santander</p>
          <p>info&#64;migajeros.com</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2025 MigajerosBucaramanga. Todos los derechos reservados.</p>
      </div>
    </footer>
  `,
  styles: [`
    .footer {
      background-color: #1a1a1a;
      color: white;
      padding: 3rem 0 1rem;
      margin-top: auto;
      border-top: 4px solid var(--secondary-color);
    }
    .footer-content {
      max-width: 1200px;
      margin: 0 auto;
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 2rem;
      padding: 0 2rem;
    }
    .footer-section h3 {
      color: var(--secondary-color);
      margin-bottom: 1rem;
    }
    .footer-section h4 {
      color: var(--primary-light);
      margin-bottom: 1rem;
    }
    .footer-section ul {
      list-style: none;
      padding: 0;
    }
    .footer-section ul li {
      margin-bottom: 0.5rem;
    }
    .footer-section a {
      color: #ccc;
      text-decoration: none;
      transition: color 0.3s;
    }
    .footer-section a:hover {
      color: var(--secondary-color);
    }
    .footer-bottom {
      text-align: center;
      margin-top: 2rem;
      padding-top: 1rem;
      border-top: 1px solid #333;
      color: #888;
    }
  `]
})
export class FooterComponent {}
